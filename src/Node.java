
class Node<T>
{
    public T data;
    public Node<T>[] forward;

    public Node(T data, int level)
    {
        this.data = data;
        this.forward = new Node[level + 1];
    }

    @Override
    public String toString()
    {
        if (data != null)
        {
            return data.toString();
        }
        return "null";
    }
}