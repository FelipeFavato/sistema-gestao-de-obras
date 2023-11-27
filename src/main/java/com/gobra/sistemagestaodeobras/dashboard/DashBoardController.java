package com.gobra.sistemagestaodeobras.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dashboard.dto.AcumuladoGastosDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.GastoFornecedorDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.GastoSocioDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.ItemLocalDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.MDOOrcamentoDTO;
import com.gobra.sistemagestaodeobras.dashboard.dto.ObraCodNomeDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.repository.ItemCompraRepository;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashBoardController {
  
  @Autowired
  private ItemCompraRepository itemCompraRepository;

  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private SocioRepository socioRepository;

  @Autowired
  private ObraRepository obraRepository;

  @GetMapping("/valorlocaluso")
  @ResponseBody
  public List<ItemLocalDTO> getAll (@RequestParam Integer codigo) {
    List<ItemLocalDTO> itemLocalList = itemCompraRepository.obterSomaValorPorLocalUso(codigo).stream().map(ItemLocalDTO::new).toList();
    return itemLocalList;
  }

  @GetMapping("/acumuladogastos")
  public List<AcumuladoGastosDTO> getAllAcumulado (@RequestParam Integer codigo) {
    List<AcumuladoGastosDTO> acumuladoList = compraRepository.obterValorAcumuladoGastos(codigo).stream().map(AcumuladoGastosDTO::new).toList();
    return acumuladoList;
  }

  @GetMapping("/gastosocio")
  public List<GastoSocioDTO> getAllGastoSocio (@RequestParam Integer codigo) {
    List<GastoSocioDTO> gastoSocioList = socioRepository.obterValorGastoPorSocio(codigo).stream().map(GastoSocioDTO::new).toList();
    return gastoSocioList;
  }

  @GetMapping("/gastofornecedor")
  public List<GastoFornecedorDTO> getAllGastoFornecedor (@RequestParam Integer codigo) {
    List<GastoFornecedorDTO> gastoFornecedorList = compraRepository.obterValorGastoPorFornecedor(codigo).stream().map(GastoFornecedorDTO::new).toList();
    return gastoFornecedorList;
  }

  @GetMapping("/mdogastoorcamento")
  public List<MDOOrcamentoDTO> getAllMDOorcamento (@RequestParam Integer codigo) {
    List<MDOOrcamentoDTO> mdoOrcamentoList = itemCompraRepository.obterMDOGastoComprasOrcamento(codigo).stream().map(MDOOrcamentoDTO::new).toList();
    return mdoOrcamentoList;
  }

  @GetMapping("/obracodnome")
  public List<ObraCodNomeDTO> getAllObraCodNome () {
    List<ObraCodNomeDTO> obraCodNomeList = obraRepository.obterCodigoNomeObras().stream().map(ObraCodNomeDTO::new).toList();
    return obraCodNomeList;
  }

}
