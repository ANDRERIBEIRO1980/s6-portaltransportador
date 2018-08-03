package br.com.viavarejo.application.service.impl;

import static br.com.viavarejo.util.Utils.isNull;
import static java.lang.String.format;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import br.com.viavarejo.application.service.ArquivoRecuperavelService;
import br.com.viavarejo.domain.model.mongodb.kitcoleta.PedidoKitColeta;

@Service
public class ArquivoFtpServiceImpl implements ArquivoRecuperavelService {

	private final Logger logger = Logger.getLogger(ArquivoFtpServiceImpl.class);
	private static final String MSG_ERRO_FTP_DO_ARQUIVO = "Falha ao efetuar FTP do arquivo: %1$s; Erro: %2$s";

	@Value("${kitcoleta.ftp.serverftp}")
	private String serverFtp;

	@Value("${kitcoleta.ftp.serverport}")
	private int serverFtpPort;

	@Value("${kitcoleta.ftp.user}")
	private String userFtp;

	@Value("${kitcoleta.ftp.password}")
	private String passwordFtp;

	@Value("${kitcoleta.ftp.path-local-tmp}")
	private String diretorioLocalTmp;

	@Override
	public String baixarArquivoZipFtp(final String diretorioArquivo, final String nomeArquivo, final String pedido) {
		return this.sftpArquivo(diretorioArquivo, nomeArquivo, pedido);
	}

	private String sftpArquivo(final String diretorioRemoto, final String arquivoRemoto, final String pedido) {

		final JSch jsch = new JSch();
		Session session = null;
		String retorno = null;

		try {
			session = jsch.getSession(this.userFtp, this.serverFtp, this.serverFtpPort);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(this.passwordFtp);
			session.connect();

			final Channel channel = session.openChannel("sftp");
			channel.connect();
			final ChannelSftp sftpChannel = (ChannelSftp) channel;

			sftpChannel.cd(diretorioRemoto);

			final File downloadFile = this.criaArquivoTemporario(arquivoRemoto, pedido);

			final OutputStream arqFtpTemporario = new BufferedOutputStream(new FileOutputStream(downloadFile));
			sftpChannel.get(arquivoRemoto, arqFtpTemporario);
			arqFtpTemporario.close();

			retorno = downloadFile.getAbsolutePath();
			this.logger.info("Arquivo baixado com sucesso em: " + retorno);
			this.desconectaSftp(session, sftpChannel);

		} catch (final Exception ex) {
			this.logger.error(format(MSG_ERRO_FTP_DO_ARQUIVO, arquivoRemoto, ex.getLocalizedMessage()));
		}
		return retorno;
	}

	private File criaArquivoTemporario(final String arquivoRemoto, final String pedido) {
		final File diretorio = new File(this.diretorioLocalTmp.concat(File.separator + pedido + File.separator));
		diretorio.mkdir();
		final String nomeArquivoTmp = this.diretorioLocalTmp.concat(File.separator + pedido + File.separator)
				.concat(arquivoRemoto).replaceAll(" ", "-");
		final File downloadFile = new File(nomeArquivoTmp);
		return downloadFile;
	}

	@Override
	public void descompactaArquivoZip(final String arquivo, final PedidoKitColeta reversa) {
		final byte[] buffer = new byte[1024];
		try {
			final String diretorio = this.recuperaDiretorio(arquivo);
			final ZipInputStream zinstream = new ZipInputStream(new FileInputStream(arquivo));
			ZipEntry zentry = zinstream.getNextEntry();
			while (zentry != null) {
				final String entryName = zentry.getName();
				final FileOutputStream outstream = new FileOutputStream(diretorio + entryName);
				int n;
				while ((n = zinstream.read(buffer)) > -1) {
					outstream.write(buffer, 0, n);
				}
				outstream.close();
				zinstream.closeEntry();
				zentry = zinstream.getNextEntry();

				this.atualizaArquivoBase64(diretorio, entryName, reversa);

			}
			zinstream.close();
			this.logger.info("Arquivo descompactado com sucesso.");
		} catch (final IOException ex) {
			ex.printStackTrace();
		}

	}

	private void atualizaArquivoBase64(final String diretorio, final String entryName, final PedidoKitColeta reversa) {
		final String arqBase64 = this.converteArquivoParaBase64(diretorio + entryName);

		if (entryName.contains("NFe") && entryName.endsWith(".pdf")) {
			reversa.setArquivoDanfeEmBase64(arqBase64);
			reversa.setNomeArquivoDanfe(entryName);
		} else if (entryName.endsWith(".pdf") && !entryName.contains("NFe")) {
			reversa.setArquivoLaudoEmBase64(arqBase64);
			reversa.setNomeArquivoLaudo(entryName);
		} else if (entryName.endsWith(".txt") && entryName.contains("NOT")) {
			reversa.setArquivoNotfisEmBase64(arqBase64);
			reversa.setNomeArquivoNotfis(entryName);
		}
	}

	private String recuperaDiretorio(final String arquivoZip) {
		final File arquivo = new File(arquivoZip);
		return arquivo.getParent().concat(File.separator);
	}

	private void desconectaSftp(final Session session, final ChannelSftp sftpChannel) {
		if (sftpChannel.isConnected()) {
			sftpChannel.exit();
		}
		if (session.isConnected()) {
			session.disconnect();
		}
	}

	private String converteArquivoParaBase64(final String fileName) {
		if (isNull(fileName)) {
			return null;
		} else {
			final byte[] bytes = this.carregaArquivo(fileName);
			return Base64.encodeBase64String(bytes);
		}
	}

	private byte[] carregaArquivo(final String fileName) {
		final File file = new File(fileName);
		final byte[] bytes = new byte[(int) file.length()];

		try (FileInputStream fis = new FileInputStream(file)) {
			int count = 0;
			while ((count = fis.read(bytes)) > 0) {
				this.logger.debug((char) count);
			}
		} catch (final Exception e) {
			this.logger.error("Erro ao carregar arquivo: " + fileName + " Erro: " + e.getMessage());
		}
		return bytes;
	}

}
