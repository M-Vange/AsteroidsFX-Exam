module Core {
    requires Common;

    requires CommonBullet;    
    requires javafx.graphics;
    exports dk.sdu.mmmi.cbse.main;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}


