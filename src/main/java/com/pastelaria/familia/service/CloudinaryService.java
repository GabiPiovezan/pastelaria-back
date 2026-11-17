package com.pastelaria.familia.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_MIME_TYPES = List.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Valida o arquivo e realiza o upload para o Cloudinary, retornando a URL segura (HTTPS).
     *
     * @param arquivo Imagem enviada via Multipart
     * @return URL HTTPS gerada pelo Cloudinary
     */
    public String uploadImagem(MultipartFile arquivo) {
        validarArquivo(arquivo);

        try {
            byte[] bytes = arquivo.getBytes();
            validarAssinaturaRealDosBytes(bytes, arquivo.getContentType());

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "pastelaria/produtos",
                    "resource_type", "image"
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = cloudinary.uploader().upload(bytes, uploadParams);

            String secureUrl = (String) resultado.get("secure_url");
            if (secureUrl == null || secureUrl.isBlank()) {
                throw new RuntimeException("Não foi possível obter a URL segura do Cloudinary.");
            }

            return secureUrl;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o upload da imagem: " + e.getMessage(), e);
        }
    }

    /**
     * Validação básica de arquivo não nulo, tamanho e Content-Type declarado.
     */
    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de imagem não pode estar vazio.");
        }

        if (arquivo.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("O tamanho da imagem excede o limite máximo permitido de 5MB.");
        }

        String contentType = arquivo.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Apenas JPEG, PNG e WEBP são aceitos.");
        }
    }

    /**
     * Validação avançada de segurança: inspeciona os magic bytes reais e integridade da imagem,
     * impedindo ataques de arquivos maliciosos renomeados (ex: scripts disfarçados de imagem).
     */
    private void validarAssinaturaRealDosBytes(byte[] bytes, String contentType) {
        if (bytes == null || bytes.length < 12) {
            throw new IllegalArgumentException("Arquivo inválido ou corrompido.");
        }

        boolean isJpeg = (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF);
        boolean isPng = (bytes[0] == (byte) 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47
                && bytes[4] == 0x0D && bytes[5] == 0x0A && bytes[6] == 0x1A && bytes[7] == 0x0A);
        boolean isWebp = (bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F'
                && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P');

        if (!isJpeg && !isPng && !isWebp) {
            throw new IllegalArgumentException("Assinatura de arquivo inválida. O arquivo não é uma imagem JPEG, PNG ou WEBP autêntica.");
        }

        // Para JPEG e PNG, valida decodificação completa do buffer de imagem via ImageIO
        if (isJpeg || isPng) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
                BufferedImage imagem = ImageIO.read(bais);
                if (imagem == null) {
                    throw new IllegalArgumentException("O conteúdo do arquivo não pôde ser decodificado como uma imagem válida.");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Falha ao analisar a estrutura da imagem.");
            }
        }
    }
}
