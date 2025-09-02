import java.io.*;
import java.util.ArrayList;

public class SkipList<T extends Comparable<T>> // crates and maintains a skip list
{
    private static final int MAX_LEVEL = 32; // Maximum number of levels
    private int level = 0; // Current highest level of the list

    Node head = new Node(null, MAX_LEVEL); // the beginning of the list

    public int randomLevel()
    {
        int level = 0;
        while (Math.random() < 0.5 && level < MAX_LEVEL)
        {
            level++;
        }
        return level;
    }

    public void add(T newData) {
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;

        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].data.compareTo(newData) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        int newLevel = randomLevel();
        if (newLevel > level) {
            for (int i = level + 1; i <= newLevel; i++) {
                update[i] = head;
            }
            level = newLevel;
        }

        Node<T> newNode = new Node<>(newData, newLevel);
        for (int i = 0; i <= newLevel; i++) {
            newNode.forward[i] = update[i].forward[i];
            update[i].forward[i] = newNode;
        }
    }

    public Node<T> logSearch(T searchData) {
        Node<T> current = head;
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].data.compareTo(searchData) < 0) {
                current = current.forward[i];
            }
        }
        current = current.forward[0];
        if (current != null && current.data.compareTo(searchData) == 0) {
            return current;
        }

        System.out.println("NO USER FOUND");
        return null;
    }



}