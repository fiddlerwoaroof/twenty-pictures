(ns twenty-pictures.db-access
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "twenty_pictures/queries.sql")
