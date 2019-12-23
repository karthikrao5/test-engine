package com.pantheon.buffers;

import com.pantheon.models.Mesh;

public interface VBO {
    public void allocate(Mesh mesh);
    public void draw();
    public void delete();
}
