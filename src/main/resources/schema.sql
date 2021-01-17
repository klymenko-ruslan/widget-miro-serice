CREATE TABLE IF NOT EXISTS widget
(
   id BIGINT NOT NULL,
   xCoordinate INTEGER NOT NULL,
   yCoordinate INTEGER NOT NULL,
   width INTEGER NOT NULL,
   height INTEGER NOT NULL,
   lastModificationTimestamp BIGINT NOT NULL,
   zIndex INTEGER NOT NULL,
   PRIMARY KEY(id)
);
CREATE INDEX IF NOT EXISTS zIndex_index ON widget(zIndex);
CREATE INDEX IF NOT EXISTS xCoordinate_index ON widget(xCoordinate);
CREATE INDEX IF NOT EXISTS yCoordinate_index ON widget(yCoordinate);
DELETE FROM widget;
