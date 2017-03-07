package com.temenos.responder.entity.metadata.t24entity.property.term;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2013 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.temenos.responder.entity.metadata.entity.property.EntityPropertyType;
import com.temenos.responder.entity.metadata.entity.property.Term;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A Vocabulary contains a set of Terms used to describe resources.
 */
public class T24EntityPropertyMetadata {

	private String t24Name;
	private String t24Type;
	private boolean primaryKey;		// duplicated
	private String mvGroupName;
	private String svGroupName;
	private boolean multiLanguage;
	private boolean enrichment;
	private Restriction restriction;
}
