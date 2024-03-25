-------------------------ADD COLUMNS, DROP COLUMNS--------------
ALTER TABLE "project_feedback"
    ADD COLUMN "feedback_template_id" INTEGER NOT NULL
;

ALTER TABLE "project_feedback"
    DROP COLUMN "feedback"
;

-------------------------CREATE TABLES--------------------------
CREATE TABLE "common_status" (
    "id" SERIAL NOT NULL,
    "code" VARCHAR(50) NOT NULL, -- code-01
    "name" VARCHAR(255) NOT NULL, -- Advise Entry Field, Career Advisor Entry Field
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "feedback_template" (
    "id" SERIAL NOT NULL,
    "code" varchar(50) NOT NULL, -- unique, EX: Template-001, Template-002,...
    "name" varchar(250) NULL,
    "description" TEXT NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "feedback_template_attribute" (
    "id" SERIAL NOT NULL,
    "feedback_template_id" INTEGER NOT NULL,
    "group" VARCHAR(255) DEFAULT '' NULL, -- Self Evaluation (Required)
    "label" VARCHAR(255) DEFAULT '' NULL, -- Success Factors
    "required" BOOLEAN DEFAULT FALSE,
    "display" BOOLEAN DEFAULT FALSE,
    "common_status_id" INTEGER NOT NULL,
	"type" VARCHAR(255) NOT NULL, -- table, form
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "feedback_template_column" (
	"id" SERIAL NOT NULL,
	"name" varchar(50) NOT NULL, -- Success Factors; Description; no opportunity to demonstrate; demonstrated well....
	"default_value" text NOT NULL, -- true; false; good....; Adaptability;Building Relationships
	"type" VARCHAR(255) NOT NULL, -- checkbox, input, label, description, dropdown, link
	"feedback_template_attribute_id" INTEGER NOT NULL,
	"created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "project_feedback_detail" (
    "id" SERIAL NOT NULL,
    "project_feedback_id" INTEGER NOT NULL,
	"feedback_template_attribute_id" INTEGER NOT NULL,
	"name" varchar(50) NOT NULL,
	"value" text NOT NULL,
	"type" VARCHAR(255) NOT NULL, -- checkbox, input, label, comment, dropdown
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

-------------------------PRIMARY KEY, UNIQUE--------------------------

ALTER TABLE "common_status" ADD CONSTRAINT "PK_common_status"
    PRIMARY KEY ("id")
;

ALTER TABLE "feedback_template" ADD CONSTRAINT "PK_feedback_template"
    PRIMARY KEY ("id")
;

ALTER TABLE "feedback_template" ADD CONSTRAINT "UNIQ_feedback_template_code"
    UNIQUE ("code")
;

ALTER TABLE "feedback_template_attribute" ADD CONSTRAINT "PK_feedback_template_attribute"
    PRIMARY KEY ("id")
;

ALTER TABLE "project_feedback_detail" ADD CONSTRAINT "PK_project_feedback_detail"
    PRIMARY KEY ("id")
;

ALTER TABLE "feedback_template_column" ADD CONSTRAINT "PK_feedback_template_column"
    PRIMARY KEY ("id")
;



-------------------------FOREIGN KEY--------------------------
ALTER TABLE "feedback_template_attribute" ADD CONSTRAINT "FK_feedback_template_attribute_feedback_template"
    FOREIGN KEY ("feedback_template_id") REFERENCES "feedback_template" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "feedback_template_attribute" ADD CONSTRAINT "FK_feedback_template_attribute_common_status"
    FOREIGN KEY ("common_status_id") REFERENCES "common_status" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "project_feedback_detail" ADD CONSTRAINT "FK_project_feedback_detail_project_feedback"
    FOREIGN KEY ("project_feedback_id") REFERENCES "project_feedback" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "project_feedback_detail" ADD CONSTRAINT "FK_project_feedback_detail_feedback_template_attribute"
    FOREIGN KEY ("feedback_template_attribute_id") REFERENCES "feedback_template_attribute" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "feedback_template_column" ADD CONSTRAINT "FK_feedback_template_column_feedback_template_attribute"
    FOREIGN KEY ("feedback_template_attribute_id") REFERENCES "feedback_template_attribute" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "project_feedback" ADD CONSTRAINT "FK_project_feedback_feedback_template"
    FOREIGN KEY ("feedback_template_id") REFERENCES "feedback_template" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;



