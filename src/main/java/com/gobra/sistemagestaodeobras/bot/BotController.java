package com.gobra.sistemagestaodeobras.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.bot.dto.GastoSocioBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.OrcamentoBotDTO;
import com.gobra.sistemagestaodeobras.bot.dto.SocioTelegramIDBotDTO;
import com.gobra.sistemagestaodeobras.repository.CompraRepository;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;


@RestController
@Component
public class BotController {
  
  @Autowired
  private CompraRepository compraRepository;

  @Autowired
  private SocioRepository socioRepository;


  @GetMapping("orcamentobot")
  public OrcamentoBotDTO getOrcamento (@RequestParam Integer codigo) {
    List<OrcamentoBotDTO> orcamentoList = compraRepository.obterOrcamentoBot(codigo).stream().map(OrcamentoBotDTO::new).toList();
    OrcamentoBotDTO orcamento = orcamentoList.get(0);
    return orcamento;
  }

  @GetMapping("gastosociobot")
  public List<GastoSocioBotDTO> getGastoSocio (@RequestParam Integer codigo) {
    List<GastoSocioBotDTO> gastoSocioList = socioRepository.obterValorGastoPorSocioBot(codigo).stream().map(GastoSocioBotDTO::new).toList();
    return gastoSocioList;
  }

  @GetMapping("sociotelegramid")
  public List<SocioTelegramIDBotDTO> getSocioTeleID () {
    List<SocioTelegramIDBotDTO> socioTeleID = socioRepository.obterNomeSocioTelegramID().stream().map(SocioTelegramIDBotDTO::new).toList();
    return socioTeleID;
  }

}
