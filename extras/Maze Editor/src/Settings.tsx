import { Box, FormControl, InputLabel, MenuItem, Select, Slider, Typography } from "@mui/material";

interface SettingsProps {
    width: number;
    height: number;
    tile: string;
    setWidth: (width: number) => void;
    setHeight: (height: number) => void;
    setTile: (tile: string) => void;
}

const tiles = [
    "hero",
    "enemy",
    "floor",
    "wall",
    "exitDoor",
    "exitLock",
    "blueLock",
    "greenLock",
    "orangeLock",
    "yellowLock",
    "blueKey",
    "greenKey",
    "orangeKey",
    "yellowKey",
    "coin"
]

export default function Settings({ width, height, tile, setWidth, setHeight, setTile }: SettingsProps) {
    return (
        <Box>
            <Typography id="input-slider" >
                Width
            </Typography>
            <Slider
                value={width}
                onChange={(e, value) => setWidth(value as number)}
                valueLabelDisplay="auto"
                min={5}
                max={20}
            />
            <Typography id="input-slider" >
                Height
            </Typography>
            <Slider
                value={height}
                onChange={(e, value) => setHeight(value as number)}
                valueLabelDisplay="auto"
                min={5}
                max={20} />
            <FormControl fullWidth>
                <InputLabel id="demo-simple-select-label">Tile</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={tile}
                    label="Tile"
                    onChange={(e) => setTile(e.target.value as string)}
                >
                    {tiles.map((tile) => (
                        <MenuItem key={tile} value={tile}>{tile}</MenuItem>
                    ))}
                </Select>
            </FormControl>
        </Box>
    );
}