import java.util.Objects;

public class Urls {

    String urlSiteDeIntranet = "intranet.nome.com.br";
    String urlSistemaDeDocumentos = "sistema.nome.com.br";
    String urlSistemaCloud = "cloud.nome.com.br";
    String urlSistemaRemoto = "sistemaremoto.nome.com.br";
    String urlSistemaRemotoDePessoal = "sistemadepessoal.nomepessoal.com.br";
    String urlSiteInternet = "www.nome.com.br";
    String urlSiteInternetDeTerceito = "www.outronome.com.br";

    public Urls(String urlSiteDeIntranet, String urlSistemaDeDocumentos, String urlSistemaCloud, String urlSistemaRemoto, String urlSistemaRemotoDePessoal, String urlSiteInternet, String urlSiteInternetDeTerceito) {
        this.urlSiteDeIntranet = !urlSiteDeIntranet.isEmpty() ? urlSiteDeIntranet : this.urlSiteDeIntranet;
        this.urlSistemaDeDocumentos = !urlSistemaDeDocumentos.isEmpty() ? urlSistemaDeDocumentos : this.urlSistemaDeDocumentos;
        this.urlSistemaCloud = !urlSistemaCloud.isEmpty() ? urlSistemaCloud : this.urlSistemaCloud;
        this.urlSistemaRemoto = !urlSistemaRemoto.isEmpty() ? urlSistemaRemoto : this.urlSistemaRemoto;
        this.urlSistemaRemotoDePessoal = !urlSistemaRemotoDePessoal.isEmpty() ? urlSistemaRemotoDePessoal : this.urlSistemaRemotoDePessoal;
        this.urlSiteInternet = !urlSiteInternet.isEmpty() ? urlSiteInternet : this.urlSiteInternet;
        this.urlSiteInternetDeTerceito = !urlSiteInternetDeTerceito.isEmpty() ? urlSiteInternetDeTerceito : this.urlSiteInternetDeTerceito;
    }

    public Urls() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Urls url = (Urls) o;
        return Objects.equals(urlSiteDeIntranet, url.urlSiteDeIntranet) && Objects.equals(urlSistemaDeDocumentos, url.urlSistemaDeDocumentos) && Objects.equals(urlSistemaCloud, url.urlSistemaCloud) && Objects.equals(urlSistemaRemoto, url.urlSistemaRemoto) && Objects.equals(urlSistemaRemotoDePessoal, url.urlSistemaRemotoDePessoal) && Objects.equals(urlSiteInternet, url.urlSiteInternet) && Objects.equals(urlSiteInternetDeTerceito, url.urlSiteInternetDeTerceito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlSiteDeIntranet, urlSistemaDeDocumentos, urlSistemaCloud, urlSistemaRemoto, urlSistemaRemotoDePessoal, urlSiteInternet, urlSiteInternetDeTerceito);
    }

    @Override
    public String toString() {
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
