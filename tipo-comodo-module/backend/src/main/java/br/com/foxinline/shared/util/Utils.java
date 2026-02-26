package br.com.foxinline.shared.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class Utils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string.trim());
    }

    /**
     * <p>
     * É considerado vazio todo e qualquer array cujo valor seja
     * <b>null</b>, ou o tamanho seja <b>zero</b>, ou todos os elementos
     * contidos no array sejam <b>null</b>. </p>
     *
     * @param args
     * @return
     */
    public static boolean isEmpty(Object... args) {

        // Qualquer valor passado que seja nulo sera considerado como vazio
        if (args == null || args.length == 0) {
            return true;
        } else {
            // pecorre o array, se existe um item não nulo é porque o array não esta vazio
            for (Object object : args) {
                if (object != null) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNotEmpty(Object... args) {
        return !isEmpty(args);
    }

    public void enviarImagem(String accessKey, String secretKey, String filePath, String bucketName, String keyName, String region) {
        try {

//            String filePath = "/opt/CerurbPro/uploads/Imovel/..."; // Substitua pelo caminho do seu arquivo
//            String bucketName = "cerurb-pro-teste";     // Nome do bucket no S3
//            String keyName = "0212202415593009663_opt_cerurbpro_uploads_registro_47718_registro.pdf"; // Nome do arquivo no S3 (com pasta opcional)
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
//            System.setProperty("com.amazonaws.sdk.disableCertChecking", "true");

            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withRegion(region)// Substituir pela sua região, ex.: Regions.US_EAST_1
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .build();

            s3.putObject(bucketName, keyName, new File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void visualizarImagem(String accessKey, String secretKey, String filePath, String bucketName, String keyName, String region) {

//        String bucketName = "cerurb-pro-teste";
//        String keyName = "0212202415593009663_opt_cerurbpro_uploads_registro_47718_registro.pdf"; // Caminho do arquivo no S3
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region) // Região do seu bucket
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        try {
            Date expiration = new Date();
            long expTimeMillis = System.currentTimeMillis();
            expTimeMillis += 1000 * 60 * 60; // 1 hora em milissegundos
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest
                    = new GeneratePresignedUrlRequest(bucketName, keyName)
                            .withMethod(com.amazonaws.HttpMethod.GET) // Método PUT para upload
                            .withExpiration(expiration);

            URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        } catch (AmazonS3Exception e) {
        }
    }

    /*public static boolean isEmpty(BasicBean bean) {
        return bean == null || bean.getId() == null;
    }

    public static boolean isNotEmpty(BasicBean bean) {
        return !isEmpty(bean);
    }*/
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String convertDateToString(Date date, String mask) {
        SimpleDateFormat format = new SimpleDateFormat(mask);
        String result = format.format(date);
        return result;
    }

    public static String colocarMascaraCpf(String cpf) {
        if (cpf.length() > 10) {
            cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
        }
        return cpf;
    }

    public static String colocarMascaraCep(String cep) {
        if (cep.length() > 7) {
            cep = cep.substring(0, 2) + "." + cep.substring(2, 5) + "-" + cep.substring(5, 8);
        }
        return cep;
    }

    public static String colocarMascaraTelefone(String cep) {
        if (cep.length() > 9) {
            cep = "(" + cep.substring(0, 2) + ")" + cep.substring(2, 6) + "-" + cep.substring(6, 10);
        }
        return cep;
    }

    public static String colocarMascaraWhatsapp(String cep) {
        if (cep.length() > 10) {
            cep = "(" + cep.substring(0, 2) + ")" + cep.substring(2, 6) + "-" + cep.substring(6, 11);
        }
        return cep;
    }

    public static String addMascaraCnpj(String cnpj) {
        if (cnpj.length() > 12) {
            cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
        }
        return cnpj;
    }

//    public static BigDecimal converterParaBigDecimal(String valor) {
//        String valorLimpo = valor.replaceAll("\\s+", "");
//        String novoValor = valorLimpo.replace("R$", "").replaceAll(" ", "").replace(".", "").replace(',', '.').replace(" ", "");
//        return new BigDecimal(novoValor);
//    }
    public static BigDecimal converterParaBigDecimal(String valor) {
        // Remove todos os caracteres não numéricos, exceto o ponto decimal.
        String valorLimpo = valor.replaceAll("\\s+", "");
        String novoValor = valorLimpo.replace("R$", "").replaceAll(" ", "").replace(".", "").replace(',', '.').replace(" ", "");
//        valorLimpo = valorLimpo.replace(" ", "");
        // Substitui a vírgula pelo ponto como separador decimal.
//        valorLimpo = valorLimpo.replace(".", "");

//        valorLimpo = valorLimpo.replace(',', '.');
        String valorFinal = novoValor.substring(1);
        // Faz a conversão para BigDecimal.
        return new BigDecimal(valorFinal);
    }

    public static String encriptografar(String caminho) {
        caminho = caminho.replaceFirst("/", "");
        SecretKey key = Keys.hmacShaKeyFor("7f-j&CKk=coNzZc0y7_4obMP?#TfcYq%fcD0mDpenW2nc!lfGoZ|d?f&RNbDHUX6"
                .getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
                .claim("data", caminho)
                .signWith(key)
                .compact();

        return jwt;
    }


    public static String descriptografar(String token) {
        SecretKey key = Keys.hmacShaKeyFor("7f-j&CKk=coNzZc0y7_4obMP?#TfcYq%fcD0mDpenW2nc!lfGoZ|d?f&RNbDHUX6"
                .getBytes(StandardCharsets.UTF_8));

        String decodedString = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("data", String.class);
        return decodedString;
    }

    public static void compressCSV(String csvFile, String zipFile) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ArchiveOutputStream aos = new ZipArchiveOutputStream(fos);
            aos.putArchiveEntry(new ZipArchiveEntry(csvFile));

            FileInputStream fis = new FileInputStream(csvFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                aos.write(buffer, 0, len);
            }

            aos.closeArchiveEntry();
            aos.close();
            fis.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
