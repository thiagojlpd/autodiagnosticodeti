import java.util.Objects;

public class Urls {

    // Atributos que armazenam URLs específicas
    String urlSiteDeIntranet = "intranet.nome.com.br";
    String urlSistemaDeDocumentos = "sistema.nome.com.br";
    String urlSistemaCloud = "cloud.nome.com.br";
    String urlSistemaRemoto = "sistemaremoto.nome.com.br";
    String urlSistemaRemotoDePessoal = "sistemadepessoal.nomepessoal.com.br";
    String urlSiteInternet = "www.nome.com.br";
    String urlSiteInternetDeTerceito = "www.outronome.com.br";

    // Construtor que permite definir as URLs com base em parâmetros passados
    public Urls(String urlSiteDeIntranet, String urlSistemaDeDocumentos, String urlSistemaCloud, String urlSistemaRemoto, String urlSistemaRemotoDePessoal, String urlSiteInternet, String urlSiteInternetDeTerceito) {
        // Verifica se os parâmetros passados são não vazios antes de atribuir às variáveis de instância
        this.urlSiteDeIntranet = !urlSiteDeIntranet.isEmpty() ? urlSiteDeIntranet : this.urlSiteDeIntranet;
        this.urlSistemaDeDocumentos = !urlSistemaDeDocumentos.isEmpty() ? urlSistemaDeDocumentos : this.urlSistemaDeDocumentos;
        this.urlSistemaCloud = !urlSistemaCloud.isEmpty() ? urlSistemaCloud : this.urlSistemaCloud;
        this.urlSistemaRemoto = !urlSistemaRemoto.isEmpty() ? urlSistemaRemoto : this.urlSistemaRemoto;
        this.urlSistemaRemotoDePessoal = !urlSistemaRemotoDePessoal.isEmpty() ? urlSistemaRemotoDePessoal : this.urlSistemaRemotoDePessoal;
        this.urlSiteInternet = !urlSiteInternet.isEmpty() ? urlSiteInternet : this.urlSiteInternet;
        this.urlSiteInternetDeTerceito = !urlSiteInternetDeTerceito.isEmpty() ? urlSiteInternetDeTerceito : this.urlSiteInternetDeTerceito;
    }

    // Construtor padrão sem parâmetros
    public Urls() {

    }

    // Método para comparar dois objetos Urls
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Verifica se os objetos são iguais
        if (o == null || getClass() != o.getClass()) return false;  // Verifica se o objeto é nulo ou de outra classe
        Urls url = (Urls) o;  // Faz o cast do objeto para a classe Urls
        // Compara as URLs para garantir que todos os campos são iguais
        return Objects.equals(urlSiteDeIntranet, url.urlSiteDeIntranet) && Objects.equals(urlSistemaDeDocumentos, url.urlSistemaDeDocumentos) && Objects.equals(urlSistemaCloud, url.urlSistemaCloud) && Objects.equals(urlSistemaRemoto, url.urlSistemaRemoto) && Objects.equals(urlSistemaRemotoDePessoal, url.urlSistemaRemotoDePessoal) && Objects.equals(urlSiteInternet, url.urlSiteInternet) && Objects.equals(urlSiteInternetDeTerceito, url.urlSiteInternetDeTerceito);
    }

    // Método que gera um código hash para o objeto Urls
    @Override
    public int hashCode() {
        // Gera um código hash usando todas as URLs
        return Objects.hash(urlSiteDeIntranet, urlSistemaDeDocumentos, urlSistemaCloud, urlSistemaRemoto, urlSistemaRemotoDePessoal, urlSiteInternet, urlSiteInternetDeTerceito);
    }

    // Método que retorna uma representação textual do objeto
    @Override
    public String toString() {
        // Retorna uma string contendo todas as URLs de forma legível
        return "url{" +
                "urlSiteDeIntranet='" + urlSiteDeIntranet + '\'' +
                ", urlSistemaDeDocumentos='" + urlSistemaDeDocumentos + '\'' +
                ", urlSistemaCloud='" + urlSistemaCloud + '\'' +
                ", urlSistemaRemoto='" + urlSistemaRemoto + '\'' +
                ", urlSistemaRemotoDePessoal='" + urlSistemaRemotoDePessoal + '\'' +
                ", urlSiteInternet='" + urlSiteInternet + '\'' +
                ", urlSiteInternetDeTerceito='" + urlSiteInternetDeTerceito + '\'' +
                '}';
    }

    // Métodos getters e setters para acessar e modificar os valores das URLs

    public String getUrlSiteDeIntranet() {
        return urlSiteDeIntranet;
    }

    public void setUrlSiteDeIntranet(String urlSiteDeIntranet) {
        this.urlSiteDeIntranet = urlSiteDeIntranet;
    }

    public String getUrlSistemaDeDocumentos() {
        return urlSistemaDeDocumentos;
    }

    public void setUrlSistemaDeDocumentos(String urlSistemaDeDocumentos) {
        this.urlSistemaDeDocumentos = urlSistemaDeDocumentos;
    }

    public String getUrlSistemaCloud() {
        return urlSistemaCloud;
    }

    public void setUrlSistemaCloud(String urlSistemaCloud) {
        this.urlSistemaCloud = urlSistemaCloud;
    }

    public String getUrlSistemaRemoto() {
        return urlSistemaRemoto;
    }

    public void setUrlSistemaRemoto(String urlSistemaRemoto) {
        this.urlSistemaRemoto = urlSistemaRemoto;
    }

    public String getUrlSistemaRemotoDePessoal() {
        return urlSistemaRemotoDePessoal;
    }

    public void setUrlSistemaRemotoDePessoal(String urlSistemaRemotoDePessoal) {
        this.urlSistemaRemotoDePessoal = urlSistemaRemotoDePessoal;
    }

    public String getUrlSiteInternet() {
        return urlSiteInternet;
    }

    public void setUrlSiteInternet(String urlSiteInternet) {
        this.urlSiteInternet = urlSiteInternet;
    }

    public String getUrlSiteInternetDeTerceito() {
        return urlSiteInternetDeTerceito;
    }

    public void setUrlSiteInternetDeTerceito(String urlSiteInternetDeTerceito) {
        this.urlSiteInternetDeTerceito = urlSiteInternetDeTerceito;
    }
}
