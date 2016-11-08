#!/usr/bin/env python
# -*- coding: utf-8 -*-
# -*- mode: python -*-
"""
Copyright Steen Manniche 2012

This file is part of itself.
addheader.py is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
addheader.py is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with addheader.py. If not, see http://www.gnu.org/licenses/.
"""
__author__ = ' <steen@manniche.net>'

import os
import sys
import fnmatch
import license_dicts
import re

def write_header( path, var_dict ):
    """Add the required copyright and license header to module in path"""

    if has_license( path ):
       return False

    ft = ""
    with open( path, 'r' ) as candidate:
        if var_dict[ 'postfix' ] == 'java':
            cmt_start = '/**'
            cmt_mid   = '*'
            cmt_end   = '*/'
        elif var_dict[ 'postfix' ] == 'py':
            cmt_start = cmt_mid = cmt_end = '#'

        licensetext = os.linesep.join( license_dicts.license_texts[ var_dict.get( 'license' ) ].split( os.linesep ) )
        
        if var_dict.get( 'license' ) == 'gpl':
            licensetext =  licensetext%( var_dict.get( 'year' ),
                                         var_dict.get( 'author' ),
                                         var_dict.get( 'project_name' ),
                                         var_dict.get( 'project_name' ),
                                         var_dict.get( 'project_name' ),
                                         var_dict.get( 'project_name' )
                                         )

        nt = cmt_start
        licensetext = [ '%s%s'%( cmt_mid, l ) for l in licensetext.split( os.linesep ) ]
        nt = nt + os.linesep.join( licensetext ) + cmt_end
        
        ft = candidate.read()
        ft = nt + ft
        
    with open( path, 'w' ) as new:
        new.write( ft )

    return True


def add_header( options, path ):
    """
    
    Arguments:
    - `type`:
    - `year`:
    """

    target = path[0]
    var_dict = set_vars( options )

    for (root, dirs, files) in os.walk(target):
        # if root.find(os.sep + '.') != -1: # skip all dot dirs
        #     print "Skipping %s"%( root )
        #     continue
        for name in files:
            path = os.path.join( root, name )
            if os.path.islink( path ): #skip symlinks
                continue
            sys.stderr.write( 'Checking if %s is of type %s%s' % ( path, options.filetype, os.linesep ) )
            if path.find( var_dict.get( 'postfix' ) ) > 0:
                sys.stderr.write( 'Match for %s found in %s%s' % ( var_dict.get( 'postfix' ), path, os.linesep ) )
                if not options.dryrun:
                    wrote_header = write_header( path, var_dict )
                if not has_license( path ) and options.dryrun:
                    sys.stderr.write( "Would have written %s license to %s%s"%( var_dict.get( 'license' ), path, os.linesep ) )

def has_license( file_to_check ):
    """
   
    Arguments:
    - `file_to_check`:
    """
    gpl_re = "under the terms of the GNU General Public License"
    asl_re = " http:\/\/www.apache.org\/licenses\/LICENSE-2\\.0"
    generic_re = "[Cc]opyright\s\d{4}\s\w+"

    with open( file_to_check ) as ftc:
        text = ftc.read()
        if re.search( gpl_re, text ):
            sys.stderr.write( "found GPL in %s%s"%( file_to_check, os.linesep ) )
            return True
        elif re.search( asl_re, text ):
            sys.stderr.write( "found ASL in %s%s"%( file_to_check, os.linesep ) )
            return True
        elif re.search( generic_re, text ):
            sys.stderr.write( "found license in %s%s"%( file_to_check, os.linesep ) )
            return True
        else:
            sys.stderr.write( "found no license in %s%s"%( file_to_check, os.linesep ) )
            return False

def set_vars( options ):
    vars = dict()

    if options.filetype == "java":
        vars[ "postfix" ] = "java"
    elif options.filetype == "python":
        vars[ "postfix" ] = "py"

    vars[ "year" ] = options.year

    vars[ "license" ] = options.license

    if options.author:
        vars[ "author" ] = options.author

    if options.project:
        vars[ "project_name" ] = options.project
    return vars


if __name__ == '__main__':
    
    from optparse import OptionParser

    usg = """%prog [options] path

This script add license headers to files in `path` that does not already have a license header
"""

    parser = OptionParser( usage = usg)
    parser.add_option("--filetype", dest="filetype", default="java",
                      help="filetype to examine, values are {java,python}")
    parser.add_option("-a", "--author", dest="author",
                      help="Author of the source code, can be empty")
    parser.add_option("-y", "--copyrightyear",
                      dest="year", default="2012",
                      help="Copyright year(s), specify with either a single year (2012) or a range (2010-2012)")
    parser.add_option("-l", "--licensetype", dest="license",
                      help="License to apply to the source files, can be one of {gpl, asl}")
    parser.add_option("-p", "--projectname", dest="project",
                      help="Name of the project")

    parser.add_option("-n", "--dry-run", action="store_true", dest="dryrun", default=False,
                      help="Does not actually commit the actions, just writes them to std.out")

    

    (options, args) = parser.parse_args()

    if not options.license:
        sys.exit( "A license must be choosen. Run this script with -h to see available options" )
    
    if not args:
        sys.exit( "A path to start the search from must be specified. Run this script with -h to see available options" )
    if len( args ) > 1:
        sys.exit( "Only one path should be specified. Run this script with -h to see available options" )
    add_header( options, args )

