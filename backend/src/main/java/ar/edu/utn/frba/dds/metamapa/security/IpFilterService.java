package ar.edu.utn.frba.dds.metamapa.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class IpFilterService {

  private static final Logger log = LoggerFactory.getLogger(IpFilterService.class);

  @Value("${security.ip.blacklist:}")
  private String blacklistConfig;

  @Value("${security.ip.whitelist:}")
  private String whitelistConfig;

  @Value("${security.ip.whitelist.enabled:false}")
  private boolean whitelistEnabled;

  private Set<String> blacklist;
  private Set<String> whitelist;

  public boolean esIpPermitida(String clientIp) {
    if (clientIp == null || clientIp.isEmpty()) {
      log.warn("IP del cliente es nula o vacía");
      return true;
    }

    inicializarListas();

    if (whitelistEnabled) {
      boolean permitida = estaEnWhitelist(clientIp);
      if (!permitida) {
        log.warn("IP bloqueada (no está en whitelist): {}", clientIp);
      }
      return permitida;
    }

    if (estaEnBlacklist(clientIp)) {
      log.warn("IP bloqueada (está en blacklist): {}", clientIp);
      return false;
    }

    return true;
  }

  private boolean estaEnBlacklist(String ip) {
    if (blacklist == null || blacklist.isEmpty()) {
      return false;
    }

    if (blacklist.contains(ip)) {
      return true;
    }

    for (String ipBloqueada : blacklist) {
      if (ipBloqueada.contains("/")) {
        if (estaEnRangoCidr(ip, ipBloqueada)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean estaEnWhitelist(String ip) {
    if (whitelist == null || whitelist.isEmpty()) {
      return false;
    }

    if (whitelist.contains(ip)) {
      return true;
    }

    for (String ipPermitida : whitelist) {
      if (ipPermitida.contains("/")) {
        if (estaEnRangoCidr(ip, ipPermitida)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean estaEnRangoCidr(String ip, String cidr) {
    try {
      String[] parts = cidr.split("/");
      String baseIp = parts[0];
      int prefixLength = Integer.parseInt(parts[1]);

      InetAddress address = InetAddress.getByName(ip);
      InetAddress network = InetAddress.getByName(baseIp);

      byte[] addressBytes = address.getAddress();
      byte[] networkBytes = network.getAddress();

      if (addressBytes.length != networkBytes.length) {
        return false;
      }

      int mask = -1 << (32 - prefixLength);
      int addressInt = bytesToInt(addressBytes);
      int networkInt = bytesToInt(networkBytes);

      return (addressInt & mask) == (networkInt & mask);

    } catch (Exception e) {
      log.error("Error al verificar rango CIDR {}: {}", cidr, e.getMessage());
      return false;
    }
  }

  private int bytesToInt(byte[] bytes) {
    int result = 0;
    for (byte b : bytes) {
      result = (result << 8) | (b & 0xFF);
    }
    return result;
  }

  private void inicializarListas() {
    if (blacklist == null) {
      blacklist = new HashSet<>();
      if (blacklistConfig != null && !blacklistConfig.trim().isEmpty()) {
        Arrays.stream(blacklistConfig.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .forEach(blacklist::add);
        log.info("Blacklist configurada con {} IPs", blacklist.size());
      }
    }

    if (whitelist == null) {
      whitelist = new HashSet<>();
      if (whitelistConfig != null && !whitelistConfig.trim().isEmpty()) {
        Arrays.stream(whitelistConfig.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .forEach(whitelist::add);
        log.info("Whitelist configurada con {} IPs", whitelist.size());
      }
    }
  }

  public void recargarListas() {
    blacklist = null;
    whitelist = null;
    inicializarListas();
    log.info("Listas de IPs recargadas");
  }
}
