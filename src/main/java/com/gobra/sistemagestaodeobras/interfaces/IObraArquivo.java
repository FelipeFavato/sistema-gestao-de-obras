package com.gobra.sistemagestaodeobras.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gobra.sistemagestaodeobras.dto.ObraArquivoResponseDTO;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.ObraArquivo;


public interface IObraArquivo {
  ObraArquivo saveArquivo (MultipartFile file, String descricao, String nome, Obra idObra) throws Exception;

  void saveFiles (MultipartFile[] files) throws Exception;

  List<ObraArquivoResponseDTO> getAllFiles();
}
