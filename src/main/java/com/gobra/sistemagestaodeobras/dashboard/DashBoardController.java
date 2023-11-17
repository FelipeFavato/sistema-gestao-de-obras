package com.gobra.sistemagestaodeobras.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dashboard.dto.AcumuladoGastosDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.ItemLocalDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashBoardController {
  
  @Autowired
  private ItemCompraRepository itemCompraRepository;

  @Autowired
  private CompraRepository compraRepository;

  @GetMapping("/valorlocaluso")
  public List<ItemLocalDTO> getAll () {
    List<ItemLocalDTO> itemLocalList = itemCompraRepository.obterSomaValorPorLocalUso().stream().map(ItemLocalDTO::new).toList();
    return itemLocalList;
  }

  @GetMapping("/acumuladogastos")
  public List<AcumuladoGastosDTO> getAllAcumulado () {
    List<AcumuladoGastosDTO> acumuladoList = compraRepository.obterValorAcumuladoGastos().stream().map(AcumuladoGastosDTO::new).toList();
    return acumuladoList;
  }

}
