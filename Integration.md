# Integration information
------
## Integration breakdown from package: App
| Class Name | Destination | line Number                                               |
|------------|-------------|-----------------------------------------------------------|
| App.java   | Domain      | [38](/src/nz/ac/vuw/ecs/swen225/gp6/app/App.java#L38)     |
| App.java   | Persistency | [38](/src/nz/ac/vuw/ecs/swen225/gp6/app/App.java#L38)     |
| GUI.java   | Renderer    | [73](/src/nz/ac/vuw/ecs/swen225/gp6/app/gui/GUI.java#L73) |
| App.java   | Recorder    | [38](/src/nz/ac/vuw/ecs/swen225/gp6/app/App.java#L44)     |


------
## Integration breakdown from package: Domain
| Class Name          | Destination | line Number                                                |
|---------------------|-------------|------------------------------------------------------------|
|DomainController.java| App         | [18](/src/nz/ac/vuw/ecs/swen225/gp6/Domain/DomainAccess/DomainController.java#L18)|
|Domain.java          | Persistency | [27](/src/nz/ac/vuw/ecs/swen225/gp6/Domain/Domain.java#L27)|


------
## Integration breakdown from package: Persistency
| Class Name       | Destination | line Number |
|------------------|-------------|-------------|
| Persistency.java | Domain      | 396         |
| Persistency.java | Recorder    | 335         |
| Persistency.java | App         | 374         |


------
## Integration breakdown from package: Recorder
| Class Name | Destination | line Number |
|------------|-------------|-------------|
| Record.java| App         | 34          |


------
## Integration breakdown from package: Renderer
| Class Name           | SourceDestination | line Number                                                           |
|----------------------|-------------------|-----------------------------------------------------------------------|
| MazeRenderer.java    | DomainController  | [47](/src/nz/ac/vuw/ecs/swen225/gp6/renderer/MazeRenderer.java#L47)   |
| InventtoryPanel.java | DomainController  | [34](/src/nz/ac/vuw/ecs/swen225/gp6/renderer/InventoryPanel.java#L34) |
| TexturePack.java     | DomainController  | [212](/src/nz/ac/vuw/ecs/swen225/gp6/renderer/TexturePack.java#L212)  |
| LogPanel.java        | Persistency       | [40](/src/nz/ac/vuw/ecs/swen225/gp6/renderer/LogPanel.java#L40)       |

------
## Integration breakdown from package: Fuzz
| Class Name | Destination | line Number                                                  |
|------------|-------------|--------------------------------------------------------------|
| Fuzz.java  | App         | [39](/src/test/nz/ac/vuw/ecs/swen225/gp6/fuzz/Fuzz.java#L39) |
| Fuzz.java  | Actions     | [40](/src/test/nz/ac/vuw/ecs/swen225/gp6/fuzz/Fuzz.java#L40) |
| Fuzz.java  | App         | [46](/src/test/nz/ac/vuw/ecs/swen225/gp6/fuzz/Fuzz.java#L46) |