package dev.arnweb.langgraph_starter;

import dev.arnweb.langgraph_starter.agentExecutor.AgentExecutor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.StateGraph;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class LanggraphStarterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LanggraphStarterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from LangGraph Starter - HELLO-WORLD");
        AgentExecutor agentExecutor = new AgentExecutor();
        StateGraph<AgentExecutor.State> graph = agentExecutor.graphBuilder().build();
        var app = graph.compile();
        List<String> list = new ArrayList<>();
        list.add("Stating Graph");
        var result = app.invoke(Map.of(AgentExecutor.State.MESSAGES, list));
        log.info(result.toString());
        result.ifPresent(map -> {
            List<String> messages = map.getMessages();
            if (messages != null) {
                // Iterating over the messages
                for (String message : messages) {
                    log.info("Graph Status {}", message);
                }
            }
        });


    }
}
