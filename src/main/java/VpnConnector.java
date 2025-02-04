import java.io.*;

public class VpnConnector {

    // Método para conectar à VPN (simulação)
    public void connectToVpn(String username, String password) {
        System.out.println("Conectando à VPN...");
        // Lógica para conectar à VPN
        // Por exemplo, pode ser usado o comando do OpenVPN ou FortiClient conforme a sua configuração
        try {
            // Aqui seria o código para executar o cliente de VPN (FortiClient ou OpenConnect)
            // Para fins de simulação, vou usar o `Runtime.exec()` para mostrar a execução do comando
            String command = "echo " + password + " | sudo openvpn --config /caminho/para/o/config.ovpn --auth-user-pass -";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Conectado à VPN!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Falha ao conectar à VPN.");
        }
    }

    // Método para verificar se a VPN está ativa
    public boolean isVpnActive() {
        try {
            // Verifica o sistema operacional
            String os = System.getProperty("os.name").toLowerCase();

            String command;
            if (os.contains("win")) {
                // Para Windows, usamos o comando 'tasklist'
                command = "tasklist /FI \"IMAGENAME eq openvpn.exe\"";
            } else {
                // Para sistemas Unix-like, usamos o comando 'ps'
                command = "ps aux | grep openvpn";
            }

            // Executa o comando
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            // Lê a saída do comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("openvpn") || line.contains("openconnect")) {
                    return true; // O processo da VPN está em execução
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false; // Se não encontrar o processo, a VPN não está ativa
    }

    // Método para desconectar da VPN
    public void disconnectVpn() {
        System.out.println("Desconectando da VPN...");
        try {
            // Você pode usar o comando para matar o processo do OpenVPN ou outro cliente VPN
            String command = "pkill openvpn"; // Para sistemas Unix-like, mata o processo openvpn
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("VPN desconectada com sucesso.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Falha ao desconectar da VPN.");
        }
    }
}
