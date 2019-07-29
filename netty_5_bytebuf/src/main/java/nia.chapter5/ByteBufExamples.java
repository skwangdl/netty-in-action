package nia.chapter5;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * Created by kerr.
 *
 * Listing 5.1 Backing array
 *
 * Listing 5.2 Direct buffer data access
 *
 * Listing 5.3 Composite buffer pattern using ByteBuffer
 *
 * Listing 5.4 Composite buffer pattern using CompositeByteBuf
 *
 * Listing 5.5 Accessing the data in a CompositeByteBuf
 *
 * Listing 5.6 Access data
 *
 * Listing 5.7 Read all data
 *
 * Listing 5.8 Write data
 *
 * Listing 5.9 Using ByteBufProcessor to find \r
 *
 * Listing 5.10 Slice a ByteBuf
 *
 * Listing 5.11 Copying a ByteBuf
 *
 * Listing 5.12 get() and set() usage
 *
 * Listing 5.13 read() and write() operations on the ByteBuf
 *
 * Listing 5.14 Obtaining a ByteBufAllocator reference
 *
 * Listing 5.15 Reference counting
 *
 * Listing 5.16 Release reference-counted object
 */
public class ByteBufExamples {
    private final static Random random = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;
    /**
     * Listing 5.1 Backing array
     */
    @Test
    public void testHeapBuffer() {
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
        int length = heapBuf.readableBytes();
        Assert.assertTrue(offset == 0);
        Assert.assertTrue(length == 0);
    }

    /**
     * Listing 5.2 Direct buffer data access
     */
    @Test
    public void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int length = directBuf.readableBytes();
        byte[] array = new byte[length];
        directBuf.getBytes(directBuf.readerIndex(), array);
        Assert.assertTrue(directBuf.capacity() == 1024);
    }

    /**
     * Listing 5.3 Composite buffer pattern using ByteBuffer
     */
    @Test
    public void byteBufferComposite() {
        ByteBuffer header = ByteBuffer.allocate(100);
        ByteBuffer body = ByteBuffer.allocate(100);
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
        Assert.assertTrue(message2.capacity() == 200);
    }


    /**
     * Listing 5.4 Composite buffer pattern using CompositeByteBuf
     */
    @Test
    public void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        messageBuf.addComponents(headerBuf, bodyBuf);
        Assert.assertTrue(messageBuf.maxNumComponents() == 16);
    }

    /**
     * Listing 5.5 Accessing the data in a CompositeByteBuf
     */
    @Test
    public void byteBufCompositeArray() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        Assert.assertTrue(compBuf.maxNumComponents() == 16);
    }

    /**
     * Listing 5.6 Access data
     */
    @Test
    public void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        buffer.setByte(0, 65);
        buffer.setByte(1, 66);
        Assert.assertTrue(buffer.getByte(0) == 65);
        Assert.assertTrue(buffer.getByte(1) == 66);
    }

    /**
     * Listing 5.7 Read all data
     */
    @Test
    public void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        buffer.writeInt(10);
        Assert.assertTrue(buffer.readInt() == 10);
    }

    /**
     * Listing 5.8 Write data
     */
    @Test
    public void write() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
        Assert.assertTrue(buffer.capacity() == 1024);
    }

    /**
     * Listing 5.9 Using ByteProcessor to find \r
     */
    @Test
    public void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
        Assert.assertTrue(index == -1);
    }

    /**
     * Listing 5.9 Using ByteBufProcessor to find \r
     *
     * use {@link io.netty.util.ByteProcessor in Netty 4.1.x}
     */
    @Test
    public void byteBufProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
        Assert.assertTrue(index == -1);
    }

    /**
     * Listing 5.10 Slice a ByteBuf
     */
    @Test
    public void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        Assert.assertTrue(sliced.toString(utf8).equals("Netty in Action"));
        buf.setByte(0, (byte)'J');
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * Listing 5.11 Copying a ByteBuf
     */
    @Test
    public void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = buf.copy(0, 15);
        Assert.assertTrue(copy.toString(utf8).equals("Netty in Action"));
        buf.setByte(0, (byte)'J');
        assert buf.getByte(0) != copy.getByte(0);
    }

    /**
     * Listing 5.12 get() and set() usage
     */
    @Test
    public void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte)'B');
        assert (char)buf.getByte(0) == (byte)'B';
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    /**
     * Listing 5.13 read() and write() operations on the ByteBuf
     */
    @Test
    public void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        assert (char)buf.readByte() == (byte)'N';
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }

    /**
     * Listing 5.14 Obtaining a ByteBufAllocator reference
     */
    @Test
    public void obtainingByteBufAllocatorReference(){
        Channel channel = CHANNEL_FROM_SOMEWHERE; //get reference form somewhere
        ByteBufAllocator allocator = channel.alloc();
        assert allocator.buffer().capacity() == 256;
    }

    /**
     * Listing 5.15 Reference counting
     * */
    @Test
    public void referenceCounting(){
        Channel channel = CHANNEL_FROM_SOMEWHERE; //get reference form somewhere
        ByteBufAllocator allocator = channel.alloc();
        ByteBuf buffer = allocator.directBuffer();
        assert buffer.refCnt() == 1;
    }

    /**
     * Listing 5.16 Release reference-counted object
     */
    @Test
    public void releaseReferenceCountedObject(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        boolean released = buffer.release();
        assert released;
    }


}
