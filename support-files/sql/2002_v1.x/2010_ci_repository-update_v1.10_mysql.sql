USE devops_ci_repository;
SET NAMES utf8mb4;

DROP PROCEDURE IF EXISTS ci_repository_schema_update;

DELIMITER <CI_UBF>

CREATE PROCEDURE ci_repository_schema_update()
BEGIN

    DECLARE db VARCHAR(100);
    SET AUTOCOMMIT = 0;
    SELECT DATABASE() INTO db;

    IF NOT EXISTS(SELECT 1
                  FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = db
                    AND TABLE_NAME = 'T_REPOSITORY_GITHUB_TOKEN'
                    AND COLUMN_NAME = 'TYPE') THEN
    ALTER TABLE `T_REPOSITORY_GITHUB_TOKEN`
        ADD COLUMN `TYPE` varchar(32) DEFAULT 'GITHUB_APP' COMMENT 'GitHub token类型（GITHUB_APP、OAUTH_APP）';
	alter table T_REPOSITORY_GITHUB_TOKEN drop key USER_ID;
    alter table T_REPOSITORY_GITHUB_TOKEN add constraint USER_ID unique (USER_ID, TYPE);
    END IF;

    IF NOT EXISTS(SELECT 1
                  FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = db
                    AND TABLE_NAME = 'T_REPOSITORY'
                    AND COLUMN_NAME = 'ENABLE_PAC') THEN
    ALTER TABLE `T_REPOSITORY`
        ADD COLUMN `ENABLE_PAC` bit(1) DEFAULT b'0' NOT NULL COMMENT '是否开启pac';
    END IF;

    IF NOT EXISTS(SELECT 1
                  FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = db
                    AND TABLE_NAME = 'T_REPOSITORY'
                    AND COLUMN_NAME = 'PAC_PROJECT_ID') THEN
    ALTER TABLE `T_REPOSITORY`
        ADD COLUMN `PAC_PROJECT_ID` varchar(32) DEFAULT '' NOT NULL COMMENT 'PAC开启的项目ID';
    END IF;

    IF NOT EXISTS(SELECT 1
                  FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = db
                    AND TABLE_NAME = 'T_REPOSITORY'
                    AND COLUMN_NAME = 'UPDATED_USER') THEN
    ALTER TABLE `T_REPOSITORY`
        ADD COLUMN `UPDATED_USER` varchar(64) NULL DEFAULT NULL COMMENT '代码库最近修改人';
    END IF;

    IF NOT EXISTS(SELECT 1
                  FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = db
                    AND TABLE_NAME = 'T_REPOSITORY_CODE_GIT'
                    AND COLUMN_NAME = 'ENABLE_MR_BLOCK') THEN
    ALTER TABLE `T_REPOSITORY_CODE_GIT`
        ADD COLUMN `ENABLE_MR_BLOCK` bit(1) DEFAULT b'0' NOT NULL COMMENT 'PAC开启的项目ID';
    END IF;

    COMMIT;
END <CI_UBF>
DELIMITER ;
COMMIT;
CALL ci_repository_schema_update();
