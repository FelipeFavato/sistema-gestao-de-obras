package com.gobra.sistemagestaodeobras.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.interfaces.IObraArquivo;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import com.gobra.sistemagestaodeobras.repository.ObraArquivoRepository;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;
import com.gobra.sistemagestaodeobras.utils.HashByteArray;


@Service
public class ObraArquivoService implements IObraArquivo {

  @Autowired
  private ObraArquivoRepository obraArquivoRepository;

  @Autowired
  private ObraRepository obraRepository;

  public ObraArquivo saveAttachment(MultipartFile file, String descricao, String nomeArquivo, Integer idObra) throws Exception {
    byte[] conteudoArquivo = file.getBytes();
    // String descricao = "";
    // String nomeArquivo = nome;
    Obra codigoObra = obraRepository.getReferenceById(idObra);
    String hashArquivo = HashByteArray.hashBArray(conteudoArquivo);
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      if (fileName.contains("..")) {
        throw  new Exception("Filename contains invalid path sequence " + fileName);
      }
      if (file.getBytes().length > (1024 * 1024)) {
        throw new Exception("File size exceeds maximum limit");
      }
      ObraArquivo attachment = new ObraArquivo(conteudoArquivo, descricao, nomeArquivo, hashArquivo, codigoObra);
      return obraArquivoRepository.save(attachment);
    } catch (MaxUploadSizeExceededException e) {
      throw new MaxUploadSizeExceededException(file.getSize());
    } catch (Exception e) {
      throw new Exception("Could not save File: " + fileName);
    }
  }

  @Override
  public ObraArquivo saveArquivo(MultipartFile file, String descricao, String nome, Obra idObra) throws Exception {
    byte[] conteudoArquivo = file.getBytes();
    String hashArquivo = HashByteArray.hashBArray(conteudoArquivo);
    // 1. String fileName = StringUtils.cleanPath(file.getOriginalFilename()): 
    //    Obtém e limpa o nome original do arquivo usando a classe StringUtils do
    //    Spring para remover caracteres inválidos de caminho do nome do arquivo.
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

    try {
      if(fileName.contains("..")) {
        throw new Exception("Filename contains invalid path sequence " + fileName);
      }
      // codigo | conteudoArquivo | descricao | nomeArquivo | hashArquivo | idObra
      ObraArquivo attachment = new ObraArquivo(conteudoArquivo, descricao, nome, hashArquivo, idObra);
      return obraArquivoRepository.save(attachment);
    } catch (MaxUploadSizeExceededException e) {
        throw new MaxUploadSizeExceededException(file.getSize());
    } catch (Exception e) {
        throw new Exception("Could not save File: " + fileName);
    }
  }


  @Override
  public void saveFiles(MultipartFile[] files) throws Exception {
    throw new UnsupportedOperationException("Unimplemented method 'saveFiles'");
  }

  @Override
  public List<ObraArquivoResponseDTO> getAllFiles() {
    return obraArquivoRepository.findAll().stream().map(ObraArquivoResponseDTO::new).toList();
  }

  // public void saveArquivoToDB (MultipartFile file, String descricao, String nome, Obra idObra) {
  //   ObraArquivo obraArquivo = new ObraArquivo();
    
  //   String fileName = StringUtils.cleanPath(file.getOriginalFilename());
  //   if (fileName.contains("..")) {
  //     System.out.println("Arquivo inválido.");
  //   }

  //   // 
  //   // MultiPartFormData -> Campos(Chave e valor) e arquivo
  //   // FormUrlEncoded
  //   // input type="file" -> accept quais os tipos de arquivo que eu aceito
  //   // Se chegar algum arquivo que não seja nem PDF ou Imagem, barrar

  //   try {
  //     obraArquivo.setConteudoImagem(file.getBytes());
  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   }

  //   obraArquivo.setDescricao(descricao);
  //   obraArquivo.setNome(nome);
  //   obraArquivo.setIdObra(idObra);
  //   obraArquivo.setHashArquivo();

  //   obraArquivoRepository.save(obraArquivo);
  // }

  // public List<ObraArquivoResponseDTO> findAllArquivos () {
  //   List<ObraArquivoResponseDTO> listaArquivos = obraArquivoRepository.findAll().stream().map(ObraArquivoResponseDTO::new).toList();
  //   return listaArquivos;
  // }

}
