#!/usr/bin/env python3
"""Generate simple 16x16 RGBA textures for Create: Sable Interactive.
Uses only Python's standard library; run from the repository root:
    python3 tools/generate_textures.py
"""
from pathlib import Path
import struct
import zlib

OUT = Path("src/main/resources/assets/create_sable_interactive/textures/block")
OUT.mkdir(parents=True, exist_ok=True)

COLORS = {
    "warning_barrier_10": ((10, 10, 12, 255), (58, 58, 62, 255)),
    "warning_barrier_20": ((10, 10, 12, 255), (64, 64, 68, 255)),
    "warning_barrier_30": ((10, 10, 12, 255), (70, 70, 74, 255)),
    "warning_barrier_50": ((10, 10, 12, 255), (76, 76, 80, 255)),
    "giant_board": ((8, 8, 10, 255), (42, 42, 46, 255)),
    "link_node": ((10, 10, 12, 255), (48, 48, 52, 255)),
    "physic_machine_rotator": ((12, 12, 14, 255), (56, 56, 60, 255)),
    "speed_camera": ((8, 8, 10, 255), (50, 50, 54, 255)),
}


def png_rgba(path: Path, pixels):
    raw = b"".join(b"\x00" + bytes(channel for pixel in row for channel in pixel) for row in pixels)
    def chunk(kind, data):
        body = kind + data
        return struct.pack(">I", len(data)) + body + struct.pack(">I", zlib.crc32(body) & 0xffffffff)
    data = (b"\x89PNG\r\n\x1a\n"
            + chunk(b"IHDR", struct.pack(">2I5B", 16, 16, 8, 6, 0, 0, 0))
            + chunk(b"IDAT", zlib.compress(raw, 9))
            + chunk(b"IEND", b""))
    path.write_bytes(data)


def make_texture(name, dark, light):
    rows = []
    for y in range(16):
        row = []
        for x in range(16):
            if name.startswith("warning_barrier"):
                # Broad diagonal black/graphite bands; deterministic 16x16 game texture.
                color = light if ((x + 2 * y) // 4) % 2 else dark
                if x in (0, 15) or y in (0, 15):
                    color = tuple(max(0, c - 24) for c in color[:3]) + (255,)
            elif name == "giant_board":
                color = light if (y in (3, 4, 8, 9, 12) and 2 <= x <= 13) else dark
                if x in (1, 14) or y in (1, 14): color = (24, 24, 28, 255)
            elif name == "link_node":
                color = light if x in (7, 8) or y in (7, 8) else dark
            elif name == "physic_machine_rotator":
                color = light if (x in (7, 8) or y in (7, 8)) else dark
            else:  # speed camera
                color = light if (y in (2, 3, 12, 13) or x in (2, 13)) else dark
                if 5 <= y <= 10 and 5 <= x <= 10: color = (32, 32, 36, 255)
            row.append(color)
        rows.append(row)
    png_rgba(OUT / f"{name}.png", rows)


for texture, (base, accent) in COLORS.items():
    make_texture(texture, base, accent)
print(f"Wrote {len(COLORS)} textures to {OUT}")
