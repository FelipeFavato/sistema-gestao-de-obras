package com.gobra.sistemagestaodeobras.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;
import com.gobra.sistemagestaodeobras.repository.ObraArquivoRepository;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;
import com.gobra.sistemagestaodeobras.utils.HashByteArray;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ObraArquivoService {

  @Autowired
  private ObraArquivoRepository obraArquivoRepository;

  @Autowired
  private ObraRepository obraRepository;

  // Método INSERT.
  public ObraArquivo saveAttachment(MultipartFile file, String descricao, String nomeArquivo, Integer idObra) throws Exception {
    byte[] conteudoArquivo = file.getBytes();
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

  // Método GET.
  public List<ObraArquivoResponseDTO> getAllFiles() {
    return obraArquivoRepository.findAll().stream().map(ObraArquivoResponseDTO::new).toList();
  }

  // Método PUT.
  public ResponseEntity<ObraArquivo> updateObraArquivo(Integer codigo, String nomeArquivo, String descricao) {
    Optional<ObraArquivo> optionalObraArquivo = obraArquivoRepository.findById(codigo);

    if (optionalObraArquivo.isPresent()) {
      ObraArquivo obraArquivo = optionalObraArquivo.get();
      // Recupera os valores de CONTEÚDO, HASH e IDOBRA que já estão presentes e atualiza.
      obraArquivo.setConteudoArquivo(obraArquivo.getConteudoArquivo());
      obraArquivo.setHashArquivo(obraArquivo.getHashArquivo());
      obraArquivo.setIdObra(obraArquivo.getIdObra());
      obraArquivo.setNomeArquivo(nomeArquivo);
      obraArquivo.setDescricao(descricao);

      return ResponseEntity.ok(obraArquivo);
    } else {
      throw new EntityNotFoundException();
    }
  }

  // Método DELETE.
  public void deletaObraArquivo (ObraArquivo arquivo) {
    obraArquivoRepository.delete(arquivo);
  }

}
