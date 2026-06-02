module Core {
    requires Common;

    requires CommonBullet;    
    requires javafx.graphics;
    exports dk.sdu.mmmi.cbse.main;

    // Spring modules needed for dependency injection
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    // spring.web contains RestTemplate, which is used to call the ScoreService
    requires spring.web;

    opens dk.sdu.mmmi.cbse.main to spring.core, spring.beans, spring.context;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}


