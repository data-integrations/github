# GitHub batch source

Description
-----------
This plugin is used to query GitHub API.

Using this plugin users can select the data sets associated with the specified repository and collect raw level data.

Properties
----------
### Basic

**Reference Name:** Name used to uniquely identify this source for lineage, annotating metadata, etc.

**Repository owner name:** GitHub username who owns the repository from which the data is retrieved.

**Repository name:** Repository name from which the data is retrieved.

**Dataset name:** Dataset name that you would like to retrieve.

### Advanced

**GitHub API hostname:** GitHub API hostname from which the data is retrieved. Optional, for GitHub Enterprise only. 
By default, _api.github.com_

### Credentials

**Authorization token:** Authorization token to be used to authenticate to GitHub API.
