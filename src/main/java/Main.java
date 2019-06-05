import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//6. Stwórz serwer http, który na żądanie metodą get wysłane bezpośrednio na serwer (port 80) zwróci dokument html zawierający formularz z polami:
//Imię, nazwisko oraz data urodzenia (spraw, aby w polu formularza można było wpowadzać jedynie datę).
//7. Jeśli na serwer będzie wysłane żądanie metodą post (przy użyciu formularza z zadania 6), to serwer zwróci dokument zawierający tabelę z przesłanymi danymi
// (nagłówki tabeli to nazwy pól, komórki to wartości).
//8. Spraw, aby serwer pamiętał listę utworzonych osób. W przypadku wysłania żądania metodą get oprócz formularza wyświetlona jest tabela z utworzonymi osobami.
// Po wysłaniu żądania metodą post zostaje dodana do listy osoba oraz w odpowiedzi mamy dokument o treści: utworzono osobę: <imię>, <nazwisko>, <data urodzenia>.
// Oraz link prowadzący do tabeli wszystkich osób (wraz z formularzem).-->
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = ServerBootstrap.bootstrap()
                .setListenerPort(80)
                .registerHandler("/", (req, res, con) -> { ;
                    System.out.println(req.getRequestLine().getMethod().equals("POST"));
                    List<String[]> usersInTable = new ArrayList<>();
                    String users = new String();
                    if (req.getRequestLine().getMethod().equals("POST")){
                        try {
                            HttpEntity entity= ((BasicHttpEntityEnclosingRequest) req).getEntity();
                            String reqBody = EntityUtils.toString(entity);
                            String[] params = reqBody.split("&|=");
                            System.out.println(Arrays.toString(params));
                            usersInTable.add(new String[]{params[1], params[3], params[5]});
                            StringEntity resEntity = new StringEntity(table(usersInTable.get(usersInTable.size() - 1)), "UTF-8");
                            resEntity.setContentType("text/html");
                            res.setEntity(resEntity);

                        } catch(Exception e){
                            System.out.println("something goes wrong");
                        }

                    } else{
                        StringEntity stringEntity = new StringEntity(form(), "UTF-8");
                        stringEntity.setContentType("text/html");
                        res.setEntity(stringEntity);
                        res.setStatusCode(HttpStatus.SC_OK);
                    }
                })
                .create();
        httpServer.start();
    }

    private static String users(String[] user, String users) {
        return users + "    <tr>\n" +
                "        <td>" + user[0] + "</td>\n" +
                "        <td>" + user[1] + "</td>\n" +
                "        <td>" + user[2] + "</td>\n" +
                "    </tr>";
    }

    private static String table(String[] user) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Table</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table>\n" +
                "    <thead>\n" +
                "        <tr>\n" +
                "            <th>Imie</th>\n" +
                "            <th>Nazwisko</th>\n" +
                "            <th>Data</th>\n" +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td>" +user[0] + "</td>\n" +
                "        <td>" + user[1] + "</td>\n" +
                "        <td>" + user[2] + "</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String form(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"\" method=\"post\">\n" +
                "    <div>\n" +
                "        <label for=\"name\">Imię</label>\n" +
                "        <input type=\"text\" name=\"name\" id=\"name\">\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <label for=\"surname\">Nazwisko</label>\n" +
                "        <input type=\"text\" name=\"surname\" id=\"surname\">\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <label for=\"date\">Data urodzenia</label>\n" +
                "        <input type=\"date\" name=\"date\" id=\"date\">\n" +
                "    </div>\n" +
                "    <div>\n" +
                "        <input type=\"submit\" id=\"\" value=\"Wyślij\">\n" +
                "    </div>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }
    public static String table(String users){
        return "<table>\n" +
                "    <thead>\n" +
                "        <tr>\n" +
                "            <th>Imie</th>\n" +
                "            <th>Nazwisko</th>\n" +
                "            <th>Data</th>\n" +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +

                users +

                "    </tbody>\n" +
                "</table>\n";
    }
}

