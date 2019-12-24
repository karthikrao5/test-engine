package com.pantheon.core.buffers;

import com.pantheon.core.models.Mesh;

public interface VBO {
    public void allocate(Mesh mesh);
    public void draw();
    public void delete();
}
