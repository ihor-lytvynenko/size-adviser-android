import semver
import subprocess
import os
import shutil
import argparse

from github import Github
from git.repo.base import Repo

VERSION_FILE = 'VERSION'
REPOSITORY = 'ihor-lytvynenko/size-adviser-android'

def read_version():
    if not os.path.exists(VERSION_FILE):
        return '0.0.1'
    
    with open(VERSION_FILE, 'r') as file:
        version = semver.Version.parse(file.readline())
        return str(version)
    
def increment_and_save(version):
    version = semver.Version.parse(version)
    
    with open(VERSION_FILE, 'w') as file:
        file.write(str(version.bump_patch()))

def exec(command, cwd=None, mask=None):
    in_str = f' in {cwd}' if cwd else ''
    log_command = command
    if mask:
        log_command = command.replace(mask, "***")

    print(f"Running command {' '.join(log_command)}{in_str}")
    process = subprocess.run(command, text=True, stderr=subprocess.STDOUT, stdout=subprocess.PIPE, cwd=cwd)

    if process.returncode != 0:
        print(f"Command failed. {process.stdout}")
        raise Exception(f"Command '{' '.join(command)}' failed")

    return process.stdout

def make_library():
    exec(["./gradlew", "size-adviser:assembleRelease"])

    result_path = os.path.join("size-adviser", "build", "outputs", "aar", "size-adviser-release.aar")
    if not os.path.exists(result_path):
            raise Exception(f"Can't find the library file at {result_path}")

    return result_path

def make_github_release(github:Github, repo_name:str, file:str, version:str):
    print(f'Creating a github release with version {version}')
    repo = github.get_repo(repo_name)
    try:
        release = repo.get_release(version)
    except:
        release = None

    if release is None:
        release = repo.create_git_release(version, version, f'v{version}')

    print(f'uploading {file}')
    release.upload_asset(file)

def commit_changes_with_tag(local_dir:str, version:str, version_file:str, branch:str):
    print('adding files to index')
    repo = Repo(local_dir)
    repo.index.add(version_file)
    
    print('commiting files')
    repo.index.commit(f'version {version}')

    print('pushing changes')
    repo.remote().push(branch)
            

def main(token, branch):
    github = Github(login_or_token=token)
    
    version = read_version()
    zip_file = make_library()

    # TODO: add check if we've already released this vesrion
    make_github_release(github=github, repo_name=REPOSITORY, file=zip_file, version=version)
    increment_and_save(version)
    commit_changes_with_tag(local_dir='./', version=version, version_file=VERSION_FILE, branch=branch)
    
    os.remove(zip_file)


parser = argparse.ArgumentParser(description='Release script')
parser.add_argument('token', type=str, help='Github access token')
parser.add_argument('branch', type=str, help='Current branch name')

parsed_args = parser.parse_args()
main(parsed_args.token, parsed_args.branch)




