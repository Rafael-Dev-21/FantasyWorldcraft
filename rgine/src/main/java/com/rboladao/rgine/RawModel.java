package com.rboladao.rgine;

import android.opengl.GLES30;
import com.rboladao.rgine.IndexBuffer;
import com.rboladao.rgine.VertexArray;
import com.rboladao.rgine.VertexBuffer;
import com.rboladao.rgine.VertexLayout;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RawModel {
    
    private VertexArray vao;
    private IndexBuffer ibo;
    
    public RawModel(float[] vertices, VertexLayout layout, int[] indices) {
        vao = new VertexArray();
        FloatBuffer vertexData = storeArrayToFloatBuffer(vertices);
        
        vao.addBuffer(new VertexBuffer(vertexData, vertices.length * Float.BYTES), layout);
        
        IntBuffer indexData = storeArrayToIntBuffer(indices);
        
        ibo = new IndexBuffer(indexData, indices.length);
    }
    
    public void bind() {
        vao.bind();
        ibo.bind();
    }
    
    public void unbind() {
        vao.unbind();
        ibo.unbind();
    }
    
    public void finalize() {
        vao.finalize();
        ibo.finalize();
    }
    
    public VertexArray getVao() {
        return vao;
    }
    
    public int getCount() {
        return ibo.size();
    }
    
    private FloatBuffer storeArrayToFloatBuffer(float[] data) {
        FloatBuffer result = ByteBuffer.allocateDirect(data.length * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(data);
        result.flip();
        
        return result;
    }
    
    private IntBuffer storeArrayToIntBuffer(int[] data) {
        IntBuffer result = ByteBuffer.allocateDirect(data.length * Integer.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(data);
        result.flip();
        
        return result;
    }
}
