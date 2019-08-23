// -*- C++ -*-

/* svgelement.cc
 *
 * By Dan Dennedy <dan@dennedy.org> 
 *
 * Copyright (C) 2003 The libxml++ development team
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

#include "svgelement.h"

namespace SVG {

Element::Element(xmlNode* node)
  : xmlpp::Element(node)
{
	if(this->get_parent() == NULL)
		level = 1;
	else
		level = dynamic_cast<Element*>(this->get_parent())->get_level() + 1;
}
    
Element::~Element()
{}

// example custom methods
void Element::set_style(const Glib::ustring& style)
{
  set_attribute("style", style);
}
    
const Glib::ustring Element::get_style() const
{
  return get_attribute("style")->get_value();
}

int Element::get_level() const {
	return level;
}

} //namespace SVG