package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();

  String userdataUrl();

  String gatewayUrl();

  String authUrl();

}
