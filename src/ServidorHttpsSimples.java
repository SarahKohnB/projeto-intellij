import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ServidorHttpsSimples {
    public static void main(String[] args) throws Exception {
        // Cria servidor HTTP escutando na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        // Rota Principal (login)
        servidor.createContext("/", troca -> {
            enviarArquivo(troca, "index.html", "text/html");
        });

        // Rotas para páginas (aluno e professor)
        servidor.createContext("/aluno", troca -> {
            enviarArquivo(troca, "aluno.html", "text/html");
        });

        servidor.createContext("/professor", troca -> {
            enviarArquivo(troca, "professor.html", "text/html");
        });

        // rotas do CSS
        servidor.createContext("/estilo.css", troca -> enviarArquivo(troca, "estilo.css", "text/css"));
        servidor.createContext("/global.css", troca -> enviarArquivo(troca, "global.css", "text/css"));
        servidor.createContext("/aluno.css", troca -> enviarArquivo(troca, "aluno.css", "text/css"));
        servidor.createContext("/professor.css", troca -> enviarArquivo(troca, "professor.css", "text/css"));

        // Login
        servidor.createContext("/login", troca -> {
            String query = troca.getRequestURI().getQuery();

            if (query == null) {
                enviarTexto(troca, "Erro, Por favor, tente novamente");
                return;
            }

            String[] partes = query.split("&");
            String usuario = partes[0].replace("usuario=", "");
            String senha = partes[1].replace("senha=", "");
            String perfil = partes[2].replace("perfil=", "");

            // Verifica usuário
            if (!(usuario.equals("sarah") || usuario.equals("isabelly") ||
                    usuario.equals("fernanda") || usuario.equals("isabelle") ||
                    usuario.equals("leila"))) {
                enviarTexto(troca, "Usuário incorreto! Volte à página de login e tente novamente.");
                return;
            }

            // Valida senha correspondente ao usuário
            boolean senhaValida =
                    (usuario.equals("sarah") && senha.equals("47455769865")) ||
                            (usuario.equals("isabelly") && senha.equals("49210057880")) ||
                            (usuario.equals("fernanda") && senha.equals("54863586876")) ||
                            (usuario.equals("isabelle") && senha.equals("54048566881")) ||
                            (usuario.equals("leila") && senha.equals("123prof"));

            if (!senhaValida) {
                enviarTexto(troca, "Senha incorreta! Volte à página de login e tente novamente.");
                return;
            }

            // PERFIL correto de cada usuário
            String perfilCorreto = usuario.equals("leila") ? "professor" : "aluno";

            if (!perfil.equals(perfilCorreto)) {
                enviarTexto(troca,
                        " Perfil incorreto! Esse usuario pertence ao perfil: " + perfilCorreto +
                                " Volte para a pagina de login e tente novamente.");
                return;
            }

            // Se tudo estiver correto, depois redireciona
            if (perfil.equals("aluno")) {
                redirecionar(troca, "/aluno.html");
            } else {
                redirecionar(troca, "/professor.html");
            }
        });

        /* FIM DO CÓDIGO */

        servidor.start();
        System.out.println("Servidor rodando em http://localhost:8080/");
    }

    private static void redirecionar(HttpExchange troca, String destino) throws IOException {
        troca.getResponseHeaders().set("Location", destino);
        troca.sendResponseHeaders(302, -1);
    }
    private static void enviarTexto(HttpExchange troca, String texto) throws IOException{
        byte[] resposta = texto.getBytes();
        troca.sendResponseHeaders(200, resposta.length);
        OutputStream os = troca.getResponseBody();
        os.write(resposta);
        os.close();
    }
    // Envia um arquivo (HTML ou CSS)
    private static void enviarArquivo(HttpExchange troca, String caminho, String tipo) throws IOException {
        File arquivo = new File("src/" + caminho);

        if (!arquivo.exists()) {
            System.out.println("Erro 404: " + arquivo.getAbsolutePath()); //arquivo não encontrado
        }

        byte[] bytes = Files.readAllBytes(arquivo.toPath());

        troca.getResponseHeaders().set("Content-Type", tipo + "; charset=UTF-8");
        troca.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = troca.getResponseBody()) {
            os.write(bytes);
        }
    }
}
