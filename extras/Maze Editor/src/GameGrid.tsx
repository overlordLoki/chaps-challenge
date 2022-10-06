import { Box } from "@mui/material";
import { useState } from "react";
import { MeetingRoom, DoorFront, Lock, Key, MonetizationOn, SentimentVerySatisfied, SentimentVeryDissatisfied, EnhancedEncryption } from '@mui/icons-material';

interface GridProps {
    width: number;
    height: number;
    grid: Array<Array<string>>;
    tile: string;
}

const gridSize = "40px";

const tileIcons: TileIcons = {
    "hero": <SentimentVerySatisfied />,
    "enemy": <SentimentVeryDissatisfied />,
    "floor": "🟦",
    "wall": "🟫",
    "exitDoor": <DoorFront />,
    "exitLock": <EnhancedEncryption />,
    "blueLock": <Lock sx={{ color: "blue" }} />,
    "greenLock": <Lock sx={{ color: "green" }} />,
    "orangeLock": <Lock sx={{ color: "orange" }} />,
    "yellowLock": <Lock sx={{ color: "yellow" }} />,
    "blueKey": <Key sx={{ color: "blue" }} />,
    "greenKey": <Key sx={{ color: "green" }} />,
    "orangeKey": <Key sx={{ color: "orange" }} />,
    "yellowKey": <Key sx={{ color: "yellow" }} />,
    "coin": <MonetizationOn />,
}

interface TileIcons {
    [key: string]: string | JSX.Element;
}

interface StringMaps {
    [key: string]: string;
}

function useRender() {
    const [count, setCount] = useState(0);

    return () => {
        setCount(count + 1);
    }
}


export default function GameGrid({ width, height, grid, tile }: GridProps) {
    const render = useRender();
    // , backgroundColor: tileColors[grid[y]?.[x] as keyof StringMaps] || "#000000"
    return (
        <Box sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
            <Box>
                {[...Array(height)].map((_, y) => (
                    <Box key={y} sx={{ display: 'flex' }}>
                        {[...Array(width)].map((_, x) => (
                            <Box
                                key={x}
                                draggable={false}
                                sx={{ width: gridSize, height: gridSize, paddingTop: 0.75, fontSize: "large", border: '1px solid white', cursor: "pointer", textAlign: "center", userSelect: "none" }}
                                onMouseDown={() => {
                                    grid[y] ||= [];
                                    grid[y][x] = grid[y][x] === tile ? "" : tile;
                                    render();
                                }}
                                onMouseOver={(e) => {
                                    if (e.buttons == 1 || e.buttons == 3) {
                                        grid[y] ||= [];
                                        grid[y][x] = grid[y][x] === tile ? "" : tile;
                                        render();
                                    }
                                }}
                            >
                                {tileIcons[grid[y]?.[x] as keyof TileIcons] || ""}
                            </Box>
                        ))}
                    </Box>
                ))}
            </Box>
        </Box>
    );
}