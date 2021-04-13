package it.niedermann.nextcloud.deck.persistence.sync.adapters.db.migration;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * <a href="https://github.com/stefan-niedermann/nextcloud-deck/issues/923">Foreign keys don't cascade (Cards stay in the database after deleting an Account)</a>
 */
public class Migration_30_31 extends Migration {

    private static final String[] SQLs = new String[]{
            "CREATE TABLE `AccessControl_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `type` INTEGER, `boardId` INTEGER, `owner` INTEGER NOT NULL, `permissionEdit` INTEGER NOT NULL, `permissionShare` INTEGER NOT NULL, `permissionManage` INTEGER NOT NULL, `userId` INTEGER, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `AccessControl` where accountId not in (select id from `Account`)",
            "INSERT INTO `AccessControl_tmp` select * from `AccessControl`",
            "DROP TABLE `AccessControl`",
            "ALTER TABLE `AccessControl_tmp` RENAME TO `AccessControl`",
            "CREATE INDEX `acl_accId` ON `AccessControl` (`accountId`)",
            "CREATE INDEX `index_AccessControl_boardId` ON `AccessControl` (`boardId`)",
            "CREATE INDEX `index_AccessControl_accountId` ON `AccessControl` (`accountId`)",
            "CREATE INDEX `index_AccessControl_id` ON `AccessControl` (`id`)",
            "CREATE INDEX `index_AccessControl_lastModifiedLocal` ON `AccessControl` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_AccessControl_accountId_id` ON `AccessControl` (`accountId`, `id`)",
            "CREATE TABLE `Activity_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `cardId` INTEGER NOT NULL, `subject` TEXT, `type` INTEGER NOT NULL, FOREIGN KEY(`cardId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Activity` where accountId not in (select id from `Account`)",
            "INSERT INTO `Activity_tmp` select * from `Activity`",
            "DROP TABLE `Activity`",
            "ALTER TABLE `Activity_tmp` RENAME TO `Activity`",
            "CREATE INDEX `activity_accID` ON `Activity` (`accountId`)",
            "CREATE INDEX `activity_cardID` ON `Activity` (`cardId`)",
            "CREATE INDEX `index_Activity_accountId` ON `Activity` (`accountId`)",
            "CREATE INDEX `index_Activity_id` ON `Activity` (`id`)",
            "CREATE INDEX `index_Activity_lastModifiedLocal` ON `Activity` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Activity_accountId_id` ON `Activity` (`accountId`, `id`)",
            "CREATE TABLE `Attachment_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `cardId` INTEGER NOT NULL, `type` TEXT, `data` TEXT, `createdAt` INTEGER, `createdBy` TEXT, `deletedAt` INTEGER, `filesize` INTEGER NOT NULL, `mimetype` TEXT, `dirname` TEXT, `basename` TEXT, `extension` TEXT, `filename` TEXT, `localPath` TEXT, `fileId` INTEGER, FOREIGN KEY(`cardId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Attachment` where accountId not in (select id from `Account`)",
            "INSERT INTO `Attachment_tmp` select * from `Attachment`",
            "DROP TABLE `Attachment`",
            "ALTER TABLE `Attachment_tmp` RENAME TO `Attachment`",
            "CREATE INDEX `index_Attachment_cardId` ON `Attachment` (`cardId`)",
            "CREATE INDEX `index_Attachment_accountId` ON `Attachment` (`accountId`)",
            "CREATE INDEX `index_Attachment_id` ON `Attachment` (`id`)",
            "CREATE INDEX `index_Attachment_lastModifiedLocal` ON `Attachment` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Attachment_accountId_id` ON `Attachment` (`accountId`, `id`)",
            "CREATE TABLE `Board_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `ownerId` INTEGER NOT NULL, `color` INTEGER, `archived` INTEGER NOT NULL, `shared` INTEGER NOT NULL, `deletedAt` INTEGER, `permissionRead` INTEGER NOT NULL, `permissionEdit` INTEGER NOT NULL, `permissionManage` INTEGER NOT NULL, `permissionShare` INTEGER NOT NULL, FOREIGN KEY(`ownerId`) REFERENCES `User`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Board` where accountId not in (select id from `Account`)",
            "INSERT INTO `Board_tmp` select * from `Board`",
            "DROP TABLE `Board`",
            "ALTER TABLE `Board_tmp` RENAME TO `Board`",
            "CREATE INDEX `index_Board_ownerId` ON `Board` (`ownerId`)",
            "CREATE INDEX `index_Board_accountId` ON `Board` (`accountId`)",
            "CREATE INDEX `index_Board_id` ON `Board` (`id`)",
            "CREATE INDEX `index_Board_lastModifiedLocal` ON `Board` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Board_accountId_id` ON `Board` (`accountId`, `id`)",
            "CREATE TABLE `Card_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `description` TEXT, `stackId` INTEGER NOT NULL, `type` TEXT, `createdAt` INTEGER, `deletedAt` INTEGER, `attachmentCount` INTEGER NOT NULL, `userId` INTEGER, `order` INTEGER NOT NULL, `archived` INTEGER NOT NULL, `dueDate` INTEGER, `notified` INTEGER NOT NULL, `overdue` INTEGER NOT NULL, `commentsUnread` INTEGER NOT NULL, FOREIGN KEY(`stackId`) REFERENCES `Stack`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Card` where accountId not in (select id from `Account`)",
            "INSERT INTO `Card_tmp` select * from `Card`",
            "DROP TABLE `Card`",
            "ALTER TABLE `Card_tmp` RENAME TO `Card`",
            "CREATE INDEX `card_accID` ON `Card` (`accountId`)",
            "CREATE INDEX `index_Card_stackId` ON `Card` (`stackId`)",
            "CREATE INDEX `index_Card_accountId` ON `Card` (`accountId`)",
            "CREATE INDEX `index_Card_id` ON `Card` (`id`)",
            "CREATE INDEX `index_Card_lastModifiedLocal` ON `Card` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Card_accountId_id` ON `Card` (`accountId`, `id`)",
            "CREATE TABLE `DeckComment_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `objectId` INTEGER, `actorType` TEXT, `creationDateTime` INTEGER, `actorId` TEXT, `actorDisplayName` TEXT, `message` TEXT, `parentId` INTEGER, FOREIGN KEY(`objectId`) REFERENCES `Card`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`parentId`) REFERENCES `DeckComment`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `DeckComment` where accountId not in (select id from `Account`)",
            "INSERT INTO `DeckComment_tmp` select * from `DeckComment`",
            "DROP TABLE `DeckComment`",
            "ALTER TABLE `DeckComment_tmp` RENAME TO `DeckComment`",
            "CREATE INDEX `comment_accID` ON `DeckComment` (`accountId`)",
            "CREATE INDEX `index_DeckComment_objectId` ON `DeckComment` (`objectId`)",
            "CREATE INDEX `idx_comment_parentID` ON `DeckComment` (`parentId`)",
            "CREATE INDEX `index_DeckComment_accountId` ON `DeckComment` (`accountId`)",
            "CREATE INDEX `index_DeckComment_id` ON `DeckComment` (`id`)",
            "CREATE INDEX `index_DeckComment_lastModifiedLocal` ON `DeckComment` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_DeckComment_accountId_id` ON `DeckComment` (`accountId`, `id`)",
            "CREATE TABLE `Label_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `color` INTEGER NOT NULL DEFAULT 0, `boardId` INTEGER NOT NULL, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Label` where accountId not in (select id from `Account`)",
            "INSERT INTO `Label_tmp` select * from `Label`",
            "DROP TABLE `Label`",
            "ALTER TABLE `Label_tmp` RENAME TO `Label`",
            "CREATE INDEX `index_Label_boardId` ON `Label` (`boardId`)",
            "CREATE UNIQUE INDEX `idx_label_title_unique` ON `Label` (`boardId`, `title`)",
            "CREATE INDEX `index_Label_accountId` ON `Label` (`accountId`)",
            "CREATE INDEX `index_Label_id` ON `Label` (`id`)",
            "CREATE INDEX `index_Label_lastModifiedLocal` ON `Label` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Label_accountId_id` ON `Label` (`accountId`, `id`)",
            "CREATE TABLE `OcsProject_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `name` TEXT NOT NULL, FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `OcsProject` where accountId not in (select id from `Account`)",
            "INSERT INTO `OcsProject_tmp` select * from `OcsProject`",
            "DROP TABLE `OcsProject`",
            "ALTER TABLE `OcsProject_tmp` RENAME TO `OcsProject`",
            "CREATE INDEX `index_project_accID` ON `OcsProject` (`accountId`)",
            "CREATE INDEX `index_OcsProject_accountId` ON `OcsProject` (`accountId`)",
            "CREATE INDEX `index_OcsProject_id` ON `OcsProject` (`id`)",
            "CREATE INDEX `index_OcsProject_lastModifiedLocal` ON `OcsProject` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_OcsProject_accountId_id` ON `OcsProject` (`accountId`, `id`)",
            "CREATE TABLE `OcsProjectResource_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `type` TEXT, `name` TEXT, `link` TEXT, `path` TEXT, `iconUrl` TEXT, `mimetype` TEXT, `previewAvailable` INTEGER, `idString` TEXT, `projectId` INTEGER NOT NULL, FOREIGN KEY(`projectId`) REFERENCES `OcsProject`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `OcsProjectResource` where accountId not in (select id from `Account`)",
            "INSERT INTO `OcsProjectResource_tmp` select * from `OcsProjectResource`",
            "DROP TABLE `OcsProjectResource`",
            "ALTER TABLE `OcsProjectResource_tmp` RENAME TO `OcsProjectResource`",
            "CREATE INDEX `index_OcsProjectResource_id` ON `OcsProjectResource` (`id`)",
            "CREATE INDEX `index_OcsProjectResource_lastModifiedLocal` ON `OcsProjectResource` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_OcsProjectResource_accountId_id` ON `OcsProjectResource` (`accountId`, `id`, `idString`, `projectId`)",
            "CREATE INDEX `index_projectResource_accID` ON `OcsProjectResource` (`accountId`)",
            "CREATE INDEX `index_projectResource_projectId` ON `OcsProjectResource` (`projectId`)",
            "CREATE TABLE `Stack_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `title` TEXT, `boardId` INTEGER NOT NULL, `deletedAt` INTEGER, `order` INTEGER NOT NULL, FOREIGN KEY(`boardId`) REFERENCES `Board`(`localId`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `Stack` where accountId not in (select id from `Account`)",
            "INSERT INTO `Stack_tmp` select * from `Stack`",
            "DROP TABLE `Stack`",
            "ALTER TABLE `Stack_tmp` RENAME TO `Stack`",
            "CREATE INDEX `index_Stack_boardId` ON `Stack` (`boardId`)",
            "CREATE INDEX `index_Stack_accountId` ON `Stack` (`accountId`)",
            "CREATE INDEX `index_Stack_id` ON `Stack` (`id`)",
            "CREATE INDEX `index_Stack_lastModifiedLocal` ON `Stack` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_Stack_accountId_id` ON `Stack` (`accountId`, `id`)",
            "CREATE TABLE `User_tmp` (`localId` INTEGER PRIMARY KEY AUTOINCREMENT, `accountId` INTEGER NOT NULL, `id` INTEGER, `status` INTEGER NOT NULL, `lastModified` INTEGER, `lastModifiedLocal` INTEGER, `etag` TEXT, `primaryKey` TEXT, `uid` TEXT, `displayname` TEXT, FOREIGN KEY(`accountId`) REFERENCES `Account`(`id`) ON DELETE CASCADE )",
            "DELETE FROM `User` where accountId not in (select id from `Account`)",
            "INSERT INTO `User_tmp` select * from `User`",
            "DROP TABLE `User`",
            "ALTER TABLE `User_tmp` RENAME TO `User`",
            "CREATE INDEX `user_uid` ON `User` (`uid`)",
            "CREATE INDEX `index_User_accountId` ON `User` (`accountId`)",
            "CREATE INDEX `index_User_id` ON `User` (`id`)",
            "CREATE INDEX `index_User_lastModifiedLocal` ON `User` (`lastModifiedLocal`)",
            "CREATE UNIQUE INDEX `index_User_accountId_id` ON `User` (`accountId`, `id`)"
    };

    public Migration_30_31() {
        super(30, 31);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        for (String sql : SQLs) {
            database.execSQL(sql);
        }
    }
}
