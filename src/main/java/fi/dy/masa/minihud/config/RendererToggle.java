package fi.dy.masa.minihud.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.options.IConfigBoolean;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.minihud.MiniHud;
import fi.dy.masa.minihud.hotkeys.KeyCallbackToggleDebugRenderer;
import fi.dy.masa.minihud.hotkeys.KeyCallbackToggleRenderer;
import fi.dy.masa.minihud.hotkeys.KeyCallbackToggleStructures;

public enum RendererToggle implements IConfigBoolean, IHotkey
{
    DEBUG_COLLISION_BOXES               ("debugCollisionBoxEnabled",    "", "Toggles the vanilla Block Collision Boxes debug renderer", "Block Collision Boxes"),
    DEBUG_HEIGHT_MAP                    ("debugHeightMapEnabled",       "", "Toggles the vanilla Height Map debug renderer", "Height Map"),
    DEBUG_NEIGHBOR_UPDATES              ("debugNeighborsUpdateEnabled", "", "Toggles the vanilla Block Neighbor Updates debug renderer", "Block Neighbor Updates"),
    DEBUG_PATH_FINDING                  ("debugPathfindingEnabled",     "", "Toggles the vanilla Pathfinding debug renderer", "Pathfinding"),
    DEBUG_SOLID_FACES                   ("debugSolidFaceEnabled",       "", "Toggles the vanilla Block Solid Faces debug renderer", "Block Solid Faces"),
    DEBUG_WATER                         ("debugWaterEnabled",           "", "Toggles the vanilla Water debug renderer", "Water"),

    OVERLAY_BLOCK_GRID                  ("overlayBlockGrid",            "", "Toggle the Block Grid overlay renderer", "Block Grid overlay"),
    OVERLAY_CHUNK_UNLOAD_BUCKET         ("overlayChunkUnloadBucket",    "", KeybindSettings.INGAME_BOTH, "Toggle the Chunk unload bucket/priority renderer", "Chunk Unload Priority overlay"),
    OVERLAY_LIGHT_LEVEL                 ("overlayLightLevel",           "", "Toggle the Light Level overlay renderer", "Light Level overlay"),
    OVERLAY_RANDOM_TICKS_FIXED          ("overlayRandomTicksFixed",     "", "Toggle the fixed-point random ticked chunks overlay renderer", "Random Ticked Chunks (fixed) overlay"),
    OVERLAY_RANDOM_TICKS_PLAYER         ("overlayRandomTicksPlayer",    "", "Toggle the player-following random ticked chunks overlay renderer", "Random Ticked Chunks (player-following) overlay"),
    OVERLAY_REGION_FILE                 ("overlayRegionFile",           "", "Toggle the region file border overlay renderer", "Region File Border overlay"),
    OVERLAY_SLIME_CHUNKS_OVERLAY        ("overlaySlimeChunks",          "", KeybindSettings.INGAME_BOTH, "Toggle the Slime Chunk overlay renderer", "Slime Chunks overlay"),
    OVERLAY_SPAWNABLE_CHUNKS_FIXED      ("overlaySpawnableChunksFixed", "", "Toggle the location-fixed spawnable chunks overlay renderer", "Spawnable Chunks (fixed) overlay"),
    OVERLAY_SPAWNABLE_CHUNKS_PLAYER     ("overlaySpawnableChunksPlayer","", "Toggle the player-following spawnable chunks overlay renderer", "Spawnable Chunks (player-following) overlay"),
    OVERLAY_SPAWNABLE_COLUMN_HEIGHTS    ("overlaySpawnableColumnHeights","", "Toggle the spawnable column heights overlay renderer", "Spawnable column heights overlay"),
    OVERLAY_SPAWN_CHUNK_OVERLAY_REAL    ("overlaySpawnChunkReal",       "", "Toggle the spawn chunks overlay renderer", "Spawn Chunks (real) overlay"),
    OVERLAY_SPAWN_CHUNK_OVERLAY_PLAYER  ("overlaySpawnChunkPlayer",     "", "Toggle the pseudo (player-following) spawn chunks overlay renderer", "Spawn Chunks (player-following, would-be) overlay"),
    OVERLAY_STRUCTURE_MAIN_TOGGLE       ("overlayStructureMainToggle",  "", "Main toggle for all structure bounding boxes", "Structure Bounding Boxes main"),
    SHAPE_RENDERER                      ("shapeRenderer",               "", "The main toggle for the shape renderer", "Shape Renderer");

    private final String name;
    private final String prettyName;
    private final String comment;
    private final KeybindMulti keybind;
    private final boolean defaultValueBoolean;
    private String modName = "";
    private boolean valueBoolean;
    private boolean lastSavedValueBoolean;

    RendererToggle(String name, String defaultHotkey, String comment, String prettyName)
    {
        this(name, defaultHotkey, KeybindSettings.DEFAULT, comment, prettyName);
    }

    RendererToggle(String name, String defaultHotkey, KeybindSettings settings, String comment, String prettyName)
    {
        this.name = name;
        this.prettyName = prettyName;
        this.comment = comment;
        this.defaultValueBoolean = false;
        this.keybind = KeybindMulti.fromStorageString(name, defaultHotkey, settings);

        if (name.equals("overlayStructureMainToggle"))
        {
            this.keybind.setCallback(new KeyCallbackToggleStructures(this));
        }
        else if (name.startsWith("debug"))
        {
            this.keybind.setCallback(new KeyCallbackToggleDebugRenderer(this));
        }
        else
        {
            this.keybind.setCallback(new KeyCallbackToggleRenderer(this));
        }

        this.cacheSavedValue();
    }

    @Override
    public ConfigType getType()
    {
        return ConfigType.HOTKEY;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getPrettyName()
    {
        return this.prettyName;
    }

    @Override
    public String getStringValue()
    {
        return String.valueOf(this.valueBoolean);
    }

    @Override
    public String getDefaultStringValue()
    {
        return String.valueOf(this.defaultValueBoolean);
    }

    @Override
    public String getComment()
    {
        return comment != null ? this.comment : "";
    }

    @Override
    public String getModName()
    {
        return this.modName;
    }

    @Override
    public void setModName(String modName)
    {
        this.modName = modName;
        this.keybind.setModName(modName);
    }

    @Override
    public boolean getBooleanValue()
    {
        return this.valueBoolean;
    }

    @Override
    public boolean getDefaultBooleanValue()
    {
        return this.defaultValueBoolean;
    }

    @Override
    public void setBooleanValue(boolean value)
    {
        this.valueBoolean = value;
    }

    @Override
    public IKeybind getKeybind()
    {
        return this.keybind;
    }

    @Override
    public boolean isModified()
    {
        return this.valueBoolean != this.defaultValueBoolean;
    }

    @Override
    public boolean isModified(String newValue)
    {
        return String.valueOf(this.defaultValueBoolean).equals(newValue) == false;
    }

    @Override
    public boolean isDirty()
    {
        return this.lastSavedValueBoolean != this.valueBoolean || this.keybind.isDirty();
    }

    @Override
    public void cacheSavedValue()
    {
        this.lastSavedValueBoolean = this.valueBoolean;
        this.keybind.cacheSavedValue();
    }

    @Override
    public void resetToDefault()
    {
        this.valueBoolean = this.defaultValueBoolean;
    }

    @Override
    public void setValueFromString(String value)
    {
        try
        {
            this.valueBoolean = Boolean.parseBoolean(value);
        }
        catch (Exception e)
        {
            MiniHud.logger.warn("Failed to read config value for {} from the JSON config", this.getName(), e);
        }
    }

    @Override
    public void setValueFromJsonElement(JsonElement element, String configName)
    {
        try
        {
            if (element.isJsonPrimitive())
            {
                this.valueBoolean = element.getAsBoolean();
            }
            else
            {
                MiniHud.logger.warn("Failed to read config value for {} from the JSON config", configName);
            }
        }
        catch (Exception e)
        {
            MiniHud.logger.warn("Failed to read config value for {} from the JSON config", configName, e);
        }

        this.cacheSavedValue();
    }

    @Override
    public JsonElement getAsJsonElement()
    {
        return new JsonPrimitive(this.valueBoolean);
    }
}
