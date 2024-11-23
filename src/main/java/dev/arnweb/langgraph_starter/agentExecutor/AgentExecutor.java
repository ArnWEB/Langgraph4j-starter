package dev.arnweb.langgraph_starter.agentExecutor;


import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

@Slf4j
@Service
public class AgentExecutor {

    Map<String, Object> callAgent(State state) {
        log.info(state.getMessages().toString());
        List<String> list = new ArrayList<>();
        list.add("Calling Agent 1");
        return Map.of(State.MESSAGES, list);

    }

    Map<String, Object> callAgent2(State state) {
        log.info(state.getMessages().toString());
        return Map.of(State.MESSAGES, "Calling Agent 2");

    }

    public final GraphBuilder graphBuilder() {
        return new GraphBuilder();
    }

    public record Message(String message) {
    }

    public static class State extends AgentState {

        public static final String MESSAGES = "messages";
        static Map<String, Channel<?>> SCHEMA = Map.of(MESSAGES, AppenderChannel.<Message>of(ArrayList::new));

        public State(Map<String, Object> initData) {
            super(initData);
        }

        public List<String> getMessages() {
            return (this.<List<String>>value(MESSAGES).orElseGet(ArrayList::new));
        }


    }

    public class GraphBuilder {

        public StateGraph<State> build() throws GraphStateException {
            return new StateGraph<>(State.SCHEMA, State::new).addEdge(START, "action").addNode("action", node_async(AgentExecutor.this::callAgent)).addNode("action2", node_async(AgentExecutor.this::callAgent2)).addEdge("action", "action2").addEdge("action2", END);
        }

    }
}
