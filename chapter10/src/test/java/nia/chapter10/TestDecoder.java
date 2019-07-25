package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class TestDecoder {

    @Test
    public void testByteToChar(){
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            byteBuf.writeByte(i + 100);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ByteToCharDecoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        String result = embeddedChannel.readInbound().toString();
        Assert.assertTrue(result.equals("敦"));
    }

    @Test
    public void testCharToByteEncoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            byteBuf.writeChar(i + 100);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new CharToByteEncoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        System.out.println(embeddedChannel.readInbound());
    }
}
