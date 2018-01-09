package com.keita.vccs.connection;

import java.io.IOException;
import java.sql.Connection;

interface ConnectionType {
    Connection mysql() throws ClassNotFoundException, IOException;
}
