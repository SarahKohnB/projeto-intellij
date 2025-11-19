import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ServidorHttpsSimples {
    public static void main(String[] args) throws Exception {
        // Cria servidor HTTP escutando na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        /* INICIO DO CÓDIGO */
        servidor.createContext("/", troca ->{
            enviarArquivo(troca, "index.html", "text/html");
        });

        servidor.createContext("/aluno", troca ->{
            enviarArquivo(troca, "aluno.html", "text/html");
        });

        servidor.createContext("/professor", troca ->{
            enviarArquivo(troca, "professor.html", "text/html");
        });
        servidor.createContext("/perfil", troca ->{
            enviarArquivo(troca, "perfil.html", "text/html");
        });

        servidor.createContext("/estilo.css", troca ->{
            enviarArquivo(troca, "estilo.css", "text/css");
        });

        servidor.createContext("/login", troca ->{
            String query = troca.getRequestURI().getQuery();
            String[] partes;
            partes = query.split("&");
            String usuario = partes[0].replace("usuario=", "");
            String senha = partes[1].replace("senha=", "");
            String perfil = partes[2].replace("perfil=", "");

            if (usuario.equals("sarah") && senha.equals("47455769865") && perfil.equals("aluno")) {
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location", "/aluno");
                troca.sendResponseHeaders(302, -1); //deu certo o envio para a rota de sucesso, se fosse erro seria tipo (404 erro)
            }else if (usuario.equals("isabelly") && senha.equals("49210057880") && perfil.equals("aluno")) {
                    System.out.println("Acesso autorizado");
                    troca.getResponseHeaders().set("Location", "/aluno");
                    troca.sendResponseHeaders(302, -1);

            }else if (usuario.equals("fernanda") && senha.equals("54863586876") && perfil.equals("aluno")) {
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location", "/aluno");

            }else if (usuario.equals("isabelle") && senha.equals("54048566881") && perfil.equals("aluno")) {
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location", "/aluno");
                troca.sendResponseHeaders(302, -1);

            } else if (usuario.equals("leila") && senha.equals("123prof") && perfil.equals("professor")) {
                System.out.println("Acesso autorizado");
                troca.getResponseHeaders().set("Location", "/professor");
                troca.sendResponseHeaders(302, -1);
            } else{
                System.out.println("Acesso negado");
            }
            System.out.println(usuario);
            System.out.println(senha);

        });


        /* FIM DO CÓDIGO */

        servidor.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }

    // Envia um arquivo (HTML ou CSS)
    private static void enviarArquivo(com.sun.net.httpserver.HttpExchange troca, String caminho, String tipo) throws IOException {
        File arquivo = new File("src/" + caminho);
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado: " + arquivo.getAbsolutePath());
        }
        byte[] bytes = Files.readAllBytes(arquivo.toPath());
        troca.getResponseHeaders().set("Content-Type", tipo + "; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Envia resposta HTML gerada no código
    private static void enviarTexto(com.sun.net.httpserver.HttpExchange troca, String texto) throws IOException {
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        troca.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }
}