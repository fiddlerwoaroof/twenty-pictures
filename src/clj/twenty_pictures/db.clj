(ns twenty-pictures.db
  (:require [hugsql.core :as hugsql]))

(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//pgsqlserver.elangley.org:5432/twenty_pictures"
   :user "twenty_pictures"
   :password "foobar"})
