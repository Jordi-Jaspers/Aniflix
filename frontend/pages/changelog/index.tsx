import {showSearchResultsState} from "@atoms/SearchResultScreen";
import ChangelogContent from "@components/changelog/ChangelogContent";
import Header from "@components/header/Header";
import SearchResultScreen from "@components/header/search/SearchResultScreen";
import {ChangeLogFile} from "@interfaces/ChangelogFile";

import {promises as fs} from "fs";
import path from "path";

import React from "react";

import Head from "next/head";
import {useRecoilValue} from "recoil";





interface Props {
    changelogs: ChangeLogFile[]
}

const CHANGELOG_ARCHIVE_PATH = path.join(process.cwd(), 'components', 'changelog', 'archive');
export default function Changelog({changelogs}: Props) {
    const showSearchResults = useRecoilValue(showSearchResultsState)
    return (
        <div className={"relative min-h-screen w-full bg-[#1E1E25] z-0"}>
            {/*  TAB TITLE  */}
            <Head>
                <title>Aniflix | Changelog</title>
                <link rel="icon" href="/favicon.ico"/>
            </Head>
            
            {/*  HEADER  */}
            <Header/>
            
            {showSearchResults && <SearchResultScreen/>}
            <section className={"pl-4 pr-4 md:pl-6 md:pr-6 lg:pl-12 flex flex-col"}>
                <div className="flex flex-row h-fit pt-[2.5%]">
                    <h1 className="font-poppins font-semibold text-[#e5e5e5] text-4xl pb-4">
                        Changelog
                    </h1>
                </div>
                <div className={"h-fit flex flex-row border-t border-b"}>
                    <div className={"w-full flex flex-col"}>
                        {changelogs.map(changelog => {
                            return <ChangelogContent key={changelog.version} version={changelog.version} content={changelog.content}/>;
                        })}
                    </div>
                </div>
            </section>
        </div>
    )
}

export async function getServerSideProps() {
    const files = await fs.readdir(CHANGELOG_ARCHIVE_PATH);
    const changelogFiles = files.filter(file => file.endsWith('.md')).sort().reverse();
    const changelogs: ChangeLogFile[] = await Promise.all(changelogFiles.map(async file => {
        const filePath = path.join(CHANGELOG_ARCHIVE_PATH, file);
        const fileContent = await fs.readFile(filePath, 'utf-8');
        return {
            version: file.replace('.md', ''),
            content: fileContent
        }
    }));
    return {props: {changelogs: changelogs}}
}
