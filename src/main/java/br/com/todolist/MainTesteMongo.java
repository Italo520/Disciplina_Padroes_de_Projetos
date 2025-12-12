package br.com.todolist;

import br.com.todolist.log.LogEntry;
import br.com.todolist.log.LogService;
import br.com.todolist.log.AuditAction;
import br.com.todolist.log.MongoConnection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MainTesteMongo {
    public static void main(String[] args) {
        try {
            System.out.println("=== INICIANDO TESTE DE MONGO DB ===");
            System.out.println("1. Testando conexao Mongo...");
            MongoDatabase db = MongoConnection.getInstance().getDatabase();
            System.out.println("   Conectado ao database: " + db.getName());
            Document ping = db.runCommand(new Document("ping", 1));
            System.out.println("   Ping Resultado: " + ping.toJson());
            System.out.println("2. Testando insercao de Log...");
            LogEntry entry = new LogEntry(AuditAction.CREATE, "Teste de Log", "Detalhes do teste");
            LogService.getInstance().getRepository().salvarLog(entry);
            System.out.println("   Log inserido com sucesso.");
            System.out.println("3. Verificando contagem de logs...");
            long count = db.getCollection("logs").countDocuments();
            System.out.println("   Total de logs na colecao: " + count);
            if (count == 0) {
                throw new RuntimeException("Falha no Mongo: Nenhum log encontrado apos insercao");
            }

            System.out.println("=== TESTE CONCLUIDO COM SUCESSO ===");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("=== ERRO NO TESTE ===");
            e.printStackTrace();
            System.exit(1);
        }
    }
}