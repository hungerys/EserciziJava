package FtpNio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server
{
  private static int DEFAULT_PORT = 51811;

  public static void main(String[] args)
  {
    int port;
    try
    {
      port = Integer.parseInt(args[0]);
    }
    catch(RuntimeException e)
    {
      port = DEFAULT_PORT;
    }
    System.out.println("Il server è ora in ascolto sulla porta " + port);

    byte[] arrayDiByte = new byte[100];

    ServerSocketChannel serverChannel;
    Selector selector = null;
    try
    {
      serverChannel = ServerSocketChannel.open();
      ServerSocket ss = serverChannel.socket();
      InetSocketAddress address = new InetSocketAddress(port);
      ss.bind(address);
      serverChannel.configureBlocking(false);
      selector = Selector.open();
      serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    while(true)
    {
      try
      {
        assert selector != null;
        selector.select();
      }
      catch(IOException | NullPointerException e)
      {
        e.printStackTrace();
      }
      Set<SelectionKey> readyKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = readyKeys.iterator();
      while(iterator.hasNext())
      {
        SelectionKey key = iterator.next();
        iterator.remove();
        try
        {
          if(key.isAcceptable())
          {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            System.out.println("Nuova connessione da parte dal client " + client);
            client.configureBlocking(false);
            SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(100200);
            buffer.flip();
            key2.attach(buffer);
          }
          else if(key.isWritable())
          {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            if(!buffer.hasRemaining())
            {
              buffer.rewind();
              int value = buffer.getInt();
              buffer.clear();
              buffer.putInt(value + 1);
              buffer.flip();
            }
            client.write(buffer);
          }
        }
        catch(IOException e)
        {
          key.cancel();
          try
          {
            key.channel().close();
          }
          catch(IOException ex)
          {

          }
        }
      }
    }
  }
}
