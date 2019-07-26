package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
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

    @Test
    public void testToIntegerDecoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 1; i < 10; i ++) {
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ToIntegerDecoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        String result = String.valueOf(embeddedChannel.readInbound());
        Assert.assertTrue("16909060".equals(result));
    }

    @Test
    public void testToIntegerDecoder2(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 1; i < 10; i ++){
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ToIntegerDecoder2());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        String result = String.valueOf(embeddedChannel.readInbound());
        Assert.assertTrue("16909060".equals(result));
    }

    @Test
    public void testIntegerToStringDecoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 1; i < 10; i ++){
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new IntegerToStringDecoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        System.out.println(embeddedChannel.readInbound());
    }

    @Test
    public void testSafeByteMessageDecoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 1; i < 10000; i ++){
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new SafeByteToMessageDecoder());
        try{
            Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        }catch(TooLongFrameException e){
            System.out.println(e.toString());
        }
    }

    @Test
    public void testShortToByteEncoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 1; i < 10; i ++){
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ShortToByteEncoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        System.out.println(embeddedChannel.readInbound());
    }

    @Test
    public void testIntegerToStringEncoder(){
        ByteBuf byteBuf = Unpooled.buffer();
        for(int i = 1; i < 10; i ++){
            byteBuf.writeByte(i);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new IntegerToStringEncoder());
        Assert.assertTrue(embeddedChannel.writeInbound(byteBuf));
        Assert.assertTrue(embeddedChannel.finish());
        System.out.println(embeddedChannel.readInbound());
    }

    
}
