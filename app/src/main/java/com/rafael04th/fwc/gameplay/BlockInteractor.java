package com.rafael04th.fwc.gameplay;

import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.BlockHit;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.Chunk;
import com.rafael04th.fwc.world.World;

import org.joml.Vector3f;

import java.util.Optional;

public class BlockInteractor {
    private static final float PLACE_BREAK_COOLDOWN = 0.1f;
    private final World world;
    private float placeBreakTimer = 0f;

    public BlockInteractor(World world) {
        this.world = world;
    }
    public void update(float deltaTime) {
        if (placeBreakTimer > 0.001f) {
            placeBreakTimer -= deltaTime;
        }
    }
    public void breakBlock(Camera camera) {
        Optional<BlockHit> result = hit(camera, 11f);
        if (result.isPresent() && placeBreakTimer <= 0.001f) {
            BlockHit hit = result.get();
            world.set((int)hit.x, (int)hit.y, (int)hit.z, Blocks.AIR);
            placeBreakTimer = PLACE_BREAK_COOLDOWN;
        }
    }
    public void placeBlock(Camera camera, Block block) {
        Optional<BlockHit> result = hit(camera, 11f);
        if (result.isPresent() && placeBreakTimer <= 0.001f) {
            BlockHit hit = result.get();
            world.set((int)(hit.x+hit.normalX), (int)(hit.y+hit.normalY), (int)(hit.z+hit.normalZ), block);
            placeBreakTimer = PLACE_BREAK_COOLDOWN;
        }
    }

    // TODO!: maybe a Ray?
    public Optional<BlockHit> hit(Camera camera, float maxdist) {
        Vector3f O = camera.getPosition();
        Vector3f D = camera.getDirection();
        float sideDistX, sideDistY, sideDistZ;
        float deltaDistX = Math.abs(D.x) < 0.001f ? 1e30f : Math.abs(1f / D.x);
        float deltaDistY = Math.abs(D.y) < 0.001f ? 1e30f : Math.abs(1f / D.y);
        float deltaDistZ = Math.abs(D.z) < 0.001f ? 1e30f : Math.abs(1f / D.z);
        int mapX = (int)O.x;
        int mapY = (int)O.y;
        int mapZ = (int)O.z;
        int stepX, stepY, stepZ;
        float dist;

        if (D.x < 0) {
            stepX = -1;
            sideDistX = (O.x - mapX) * deltaDistX;
        } else {
            stepX = 1;
            sideDistX = (mapX + 1.0f - O.x) * deltaDistX;
        }

        if (D.y < 0) {
            stepY = -1;
            sideDistY = (O.y - mapY) * deltaDistY;
        } else {
            stepY = 1;
            sideDistY = (mapY + 1.0f - O.y) * deltaDistY;
        }

        if (D.z < 0) {
            stepZ = -1;
            sideDistZ = (O.z - mapZ) * deltaDistZ;
        } else {
            stepZ = 1;
            sideDistZ = (mapZ + 1.0f - O.z) * deltaDistZ;
        }

        int axis = 0;
        boolean hit = false;
        dist = 0;

        int accX = 0, accY = 0, accZ = 0;
        Block b = null;
        while (!hit && dist < maxdist*maxdist) {
            if (sideDistX < sideDistY && sideDistX < sideDistZ) {
                sideDistX += deltaDistX;
                mapX += stepX;
                accX += stepX;
                axis = 0;
            } else if (sideDistY < sideDistZ) {
                sideDistY += deltaDistY;
                mapY += stepY;
                accY += stepY;
                axis = 1;
            } else {
                sideDistZ += deltaDistZ;
                mapZ += stepZ;
                accZ += stepZ;
                axis = 2;
            }
            dist = accX * accX + accY * accY + accZ * accZ;
            Chunk c = world.atF(mapX, mapZ);
            int cx = mapX % Chunk.WIDTH;
            if (cx < 0) cx += Chunk.WIDTH;
            int cy = mapY % Chunk.HEIGHT;
            if (cy < 0) cy += Chunk.HEIGHT;
            int cz = mapZ % Chunk.DEPTH;
            if (cz < 0) cz += Chunk.DEPTH;
            if (c == null) {
                return Optional.empty();
            }
            if ((b = c.get(cx, cy, cz)) != Blocks.AIR) hit = true;
        }
        if (dist >= maxdist * maxdist) {
            return Optional.empty();
        }
        dist = (float)Math.sqrt(dist);

        float normalX=0, normalY=0, normalZ=0;
        switch (axis) {
            case 0:
                normalX = -stepX;
                break;
            case 1:
                normalY = -stepY;
                break;
            case 2:
                normalZ = -stepZ;
                break;
        }

        return Optional.of(new BlockHit(mapX, mapY, mapZ, normalX, normalY, normalZ, dist, b));
    }
}
