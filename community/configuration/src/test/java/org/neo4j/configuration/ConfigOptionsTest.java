/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.configuration;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.neo4j.graphdb.config.Configuration;
import org.neo4j.graphdb.config.Setting;

import static org.junit.Assert.assertEquals;

public class ConfigOptionsTest
{
    private Setting<Integer> setting = new Setting<Integer>()
    {
        @Override
        public String name()
        {
            return "myInt";
        }

        @Override
        public void withScope( Function<String,String> scopingRule )
        {

        }

        @Override
        public String getDefaultValue()
        {
            return "1";
        }

        @Override
        public Integer from( Configuration config )
        {
            return config.get( this );
        }

        @Override
        public Integer apply( Function<String,String> provider )
        {
            return Integer.parseInt( provider.apply( name() ) );
        }
    };
    private ConfigOptions configOptions;

    @Before
    public void setup()
    {
        this.configOptions = new ConfigOptions( setting, Optional.empty(), Optional.empty(), false, Optional.empty() );
    }

    @Test
    public void setting() throws Exception
    {
        assertEquals( setting, configOptions.settingGroup() );
    }

    @Test
    public void description() throws Exception
    {
        assertEquals( Optional.empty(), configOptions.description() );
    }

    @Test
    public void asConfigValue() throws Exception
    {
        List<ConfigValue> values = configOptions.asConfigValues( Collections.singletonMap( "myInt", "123" ) );

        assertEquals( 1, values.size() );

        assertEquals( Optional.of( 123 ), values.get( 0 ).value() );
        assertEquals( "myInt", values.get( 0 ).name() );
        assertEquals( Optional.empty(), values.get( 0 ).description() );
    }
}
