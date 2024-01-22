import React from 'react';

import {TagIcon} from "@heroicons/react/24/outline";
import ReactMarkdown from "react-markdown";

interface Props {
    version: string
    content: string
}

export default function ChangelogContent({version, content}: Props) {
    return (
        <div className="flex flex-row border-b w-full">
            <div className={"flex flex-col w-[10%] border-r p-4"}>
                <div className={"flex flex-row justify-end"}>
                    <TagIcon className="h-4 w-4 mt-1 mx-2"/>
                    <span className={"font-poppins text-thin"}>{version}</span>
                </div>
            </div>
            <ReactMarkdown
                className={"w-[90%] text-white font-poppins px-4 pb-4"}
                components={{
                    p: props =>
                        <p {...props} className="mb-4"/>,

                    h1: props =>
                        <h1 {...props} className="text-2xl my-4 font-semibold"/>,

                    h2: props =>
                        <h2 {...props} className="text-xl my-4 font-semibold"/>,

                    h3: props =>
                        <h3 {...props} className="text-lg my-4 font-semibold"/>,

                    h4: props =>
                        <h4 {...props} className="my-4 font-semibold"/>,

                    h5: props =>
                        <h5 {...props} className="my-4 font-semibold"/>,

                    h6: props =>
                        <h6 {...props} className="my-4 font-semibold"/>,

                    strong: props =>
                        <strong {...props} className="font-bold"/>,

                    em: props =>
                        <em {...props} className="font-italic"/>,

                    ul: ({node, ...props}) =>
                        <ul  {...props} className="list-disc list-inside ml-4"/>,

                    ol: ({node, ...props}) =>
                        <ol className="list-decimal list-inside ml-4" {...props}/>,

                    li: ({node, ...props}) =>
                        <li className="text-base ml-4" {...props}/>,

                    a: ({node, ...props}) =>
                        <a className="text-red-500" {...props}/>,

                    blockquote: ({node, ...props}) =>
                        <blockquote
                            className="text-[#d1d1d1] h-fit py-2 rounded border-l-4 border-red-500 bg-gray-800/80 px-4 ml-4 italic" {...props}/>,

                    code: ({node, ...props}) =>
                        <code className="bg-gray-800/80 p-1 text-[#d1d1d1] rounded italic text-sm" {...props}/>,

                    table: ({node, ...props}) =>
                        <table className="table-auto" {...props}/>,

                    thead: ({node, ...props}) =>
                        <thead className="bg-gray-800" {...props}/>,

                    tbody: ({node, ...props}) =>
                        <tbody className="bg-gray-700" {...props}/>,

                    tr: ({node, ...props}) =>
                        <tr className="border-b border-gray-600" {...props}/>,

                    th: ({node, ...props}) =>
                        <th className="p-2" {...props}/>,

                    td: ({node, ...props}) =>
                        <td className="p-2" {...props}/>,
                }}
            >
                {content}
            </ReactMarkdown>
            <br/>
        </div>
    )
}





