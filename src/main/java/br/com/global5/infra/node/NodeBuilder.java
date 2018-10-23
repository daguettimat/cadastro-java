package br.com.global5.infra.node;

/**
 * Created by zielinski on 16/03/17.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NodeBuilder {

    public static Node build(List<Map<String, String>> input) {

        Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();

        Map<Integer, Integer> childParentMap = new HashMap<Integer, Integer>();

        Node root = new Node(null, 0, "root",0,true,null,0);
        nodeMap.put(root.getId(), root);

        for (Map<String, String> map : input) {

            int id = Integer.parseInt(map.get("id"));
            String name = map.get("name");
            int parent = Integer.parseInt(map.get("parent"));
            boolean selectable = (map.get("selectable") == "true")  ;
            String link = map.get("link");
            int order = Integer.parseInt(map.get("order"));

            Node node = new Node(null, id, name, parent, selectable, link, order);
            nodeMap.put(id, node);

            childParentMap.put(id, parent);

        }

        for (Map.Entry<Integer, Integer> entry : childParentMap.entrySet()) {
            int nodeId = entry.getKey();
            int parentId = entry.getValue();

            Node child = nodeMap.get(nodeId);
            Node parent = nodeMap.get(parentId);
            parent.addChild(child);
        }

        return root;
    }
}
