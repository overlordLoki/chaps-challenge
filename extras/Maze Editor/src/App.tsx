import { useRef, useState } from 'react'
import { Box, Button, createTheme, CssBaseline, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, ThemeProvider, Typography } from '@mui/material'
import Settings from './Settings';
import GameGrid from './GameGrid';
import serialize from './serialize';

const theme = createTheme({
  palette: {
    // dark mode
    mode: 'dark',

  },
});

function App() {
  const [width, setWidth] = useState(10)
  const [height, setHeight] = useState(10)
  const [tile, setTile] = useState("")
  const [serialized, setSerialized] = useState("")
  const [dialogOpen, setDialogOpen] = useState(false)

  const grid = useRef<Array<Array<string>>>([]).current

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ margin: 4 }}>
        <Typography variant="h3">
          Group Project Map Editor
        </Typography>
        <Settings width={width} height={height} tile={tile} setWidth={setWidth} setHeight={setHeight} setTile={setTile} />
        <Button variant="contained" sx={{ marginTop: 2 }} onClick={() => {
          setSerialized(serialize(grid, width, height))
          setDialogOpen(true)
        }}>Serialize</Button>
      </Box>
      <GameGrid width={width} height={height} grid={grid} tile={tile} />
      <Dialog
        open={dialogOpen}
        onClose={() => setSerialized("")}
      >
        <DialogTitle>Here's your serialised grid as XML</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-slide-description">
            <pre>
              <code>
                {serialized}
              </code>
            </pre>
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>
    </ThemeProvider>
  )
}

export default App
