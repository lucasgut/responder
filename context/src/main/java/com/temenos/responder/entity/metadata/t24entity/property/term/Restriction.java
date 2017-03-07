package com.temenos.responder.entity.metadata.t24entity.property.term;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2014 Temenos Holdings N.V.
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

import com.temenos.responder.entity.metadata.entity.property.Term;

public enum Restriction {
	DISPLAYONLY ("displayOnly"),
	FILTEREONLY ("filterOnly"),
	// just so that we do not have null
	NORESTRICTION ("NoRestriction");

	private String restrictionValue;

	private Restriction(String restrictionValue) {
		this.restrictionValue = restrictionValue;
	}

	public String getValue() {
		return restrictionValue;
	}

	// Retrieve the type of the Restriction we have
	public static Restriction getRestrictionType(String restriction) {
		if (restriction != null && !restriction.isEmpty()) {
			if (restriction.equalsIgnoreCase(Restriction.DISPLAYONLY.getValue()))
				return DISPLAYONLY;
			else if (restriction.equalsIgnoreCase(Restriction.FILTEREONLY.getValue()))
				return FILTEREONLY;
		}
		return NORESTRICTION;
	}
};