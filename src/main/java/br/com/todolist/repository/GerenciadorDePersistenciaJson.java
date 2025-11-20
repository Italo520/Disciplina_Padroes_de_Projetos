package br.com.todolist.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Classe utilitária para gerenciar a persistência de dados em arquivos JSON.
 * Utiliza a biblioteca Jackson para serialização e desserialização de objetos.
 */
public class GerenciadorDePersistenciaJson {

    private final ObjectMapper objectMapper;
    private final File arquivo;

    /**
     * Construtor da classe GerenciadorDePersistenciaJson.
     * Configura o ObjectMapper e o arquivo de destino.
     *
     * @param nomeArquivo O caminho e nome do arquivo JSON onde os dados serão salvos.
     */
    public GerenciadorDePersistenciaJson(String nomeArquivo) {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        this.arquivo = new File(nomeArquivo);
    }

    /**
     * Salva um objeto no arquivo JSON configurado.
     *
     * @param objeto O objeto a ser serializado e salvo.
     */
    public void salvar(Object objeto) {
        // Garante que o diretório pai exista
        if (arquivo.getParentFile() != null && !arquivo.getParentFile().exists()) {
            arquivo.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(arquivo)) {
            objectMapper.writeValue(writer, objeto);
            System.out.println("Dados salvos com sucesso em " + arquivo.getName());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados do arquivo JSON e os converte para o tipo especificado.
     *
     * @param tipoDeDados O tipo da classe para a qual os dados devem ser desserializados.
     * @param <T>         O tipo genérico do retorno.
     * @return O objeto desserializado, ou null se o arquivo não existir ou ocorrer erro.
     */
    public <T> T carregar(Type tipoDeDados) {
        if (!arquivo.exists() || arquivo.length() == 0) {
            return null;
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return objectMapper.readValue(reader, objectMapper.constructType(tipoDeDados));
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo JSON: " + e.getMessage());
            return null;
        }
    }
}
